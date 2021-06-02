package main.service;

import main.api.request.SurveyRequest;
import main.api.response.SurveyProcessResponse;
import main.api.response.SurveyResponse;
import main.dto.QuestionDto;
import main.model.Question;
import main.model.QuestionType;
import main.model.Survey;
import main.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    @Value("${survey.min.title.length:6}")
    private double minTitleLength;

    @Autowired
    public SurveyService(final SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Optional<SurveyResponse> getSurveyByIdResponse(final int id) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            return Optional.of(getSurveyResponse(optionalSurvey.get()));
        } else {
            return Optional.empty();
        }
    }

    public SurveyProcessResponse getSurveyCreateResponse(final SurveyRequest surveyRequest) {
        Optional<HashMap<String, String>> optionalErrors = checkSurveyRequest(surveyRequest);
        if (optionalErrors.isPresent()) {
            return new SurveyProcessResponse(false, optionalErrors.get());
        } else {
            addNewSurvey(surveyRequest);
            return new SurveyProcessResponse(true);
        }
    }

    public SurveyProcessResponse getSurveyEditResponse(final int id, final SurveyRequest surveyRequest) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Optional<HashMap<String, String>> optionalErrors = checkSurveyRequest(surveyRequest);
            if (optionalErrors.isPresent()) {
                return new SurveyProcessResponse(false, optionalErrors.get());
            } else {
                setEditableSurvey(optionalSurvey.get(), surveyRequest);
                return new SurveyProcessResponse(true);
            }
        } else {
            return new SurveyProcessResponse(false);
        }
    }

    public SurveyProcessResponse getSurveyDeleteResponse(final int id) {
        if (surveyRepository.existsById(id)) {
            surveyRepository.deleteById(id);
            return new SurveyProcessResponse(true);
        } else {
            return new SurveyProcessResponse(false);
        }
    }

    private Optional<HashMap<String, String>> checkSurveyRequest(final SurveyRequest surveyRequest) {
        HashMap<String, String> errors = new HashMap<>();
        if (surveyRequest.getTitle().length() < minTitleLength) {
            errors.put("title", "Заголовок короче " + minTitleLength + " символов");
        }
        return errors.size() > 0 ? Optional.of(errors) : Optional.empty();
    }

    private void addNewSurvey(final SurveyRequest surveyRequest) {
        Survey survey = new Survey();
        survey.setTitle(surveyRequest.getTitle());
        survey.setStartDate(surveyRequest.getStartDate());
        survey.setFinishDate(surveyRequest.getFinishDate());
        survey.setDescription(surveyRequest.getDescription());
        surveyRepository.save(survey);
    }

    private void setEditableSurvey(final Survey survey, final SurveyRequest surveyRequest) {
        survey.setFinishDate(surveyRequest.getFinishDate());
        survey.setTitle(surveyRequest.getTitle());
        survey.setDescription(surveyRequest.getDescription());
        surveyRepository.save(survey);
    }

    private SurveyResponse getSurveyResponse(final Survey survey) {
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setId(survey.getId());

        List<Question> questions = survey.getQuestions();
        surveyResponse.setCount(questions.size());

        List<QuestionDto> questionDtoList = new ArrayList<>();
        questions.forEach(q -> questionDtoList.add(question2questionDto(q)));
        surveyResponse.setQuestions(questionDtoList);
        return surveyResponse;
    }

    private QuestionDto question2questionDto(final Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setText(question.getText());
        QuestionType type = question.getQuestionType();
        questionDto.setType(type.name());

        if (type.equals(QuestionType.SINGLE_CHOICE) || type.equals(QuestionType.MULTIPLE_CHOICE)) {
            questionDto.setAmountOfItems(question.getAmountOfItems());
        }
        return questionDto;
    }

}
