package main.service;

import main.api.request.CompletedSurveysRequest;
import main.api.request.QuestionsAndAnswersRequest;
import main.api.request.SurveyRequest;
import main.api.response.QuestionsAndAnswersResponse;
import main.api.response.SurveyListResponse;
import main.api.response.SurveyProcessResponse;
import main.api.response.SurveyResponse;
import main.dto.*;
import main.model.Question;
import main.model.QuestionType;
import main.model.Survey;
import main.repository.SurveyRepository;
import main.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Value("${survey.min.title.length:6}")
    private double minTitleLength;

    @Autowired
    public SurveyService(final SurveyRepository surveyRepository, final UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
    }

    public Optional<SurveyResponse> getSurveyByIdResponse(final int id) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            return Optional.of(getSurveyResponse(optionalSurvey.get()));
        } else {
            return Optional.empty();
        }
    }

    public Optional<SurveyListResponse> getActiveSurveyResponse() {
        Optional<List<Survey>> optionalSurveyList = surveyRepository.findAllActiveSurveys();
        if (optionalSurveyList.isPresent()) {
            SurveyListResponse surveyListResponse = new SurveyListResponse();
            List<Survey> surveys = optionalSurveyList.get();
            List<SurveyIdAndTitleDto> surveyIdAndTitleDtoList = new ArrayList<>();
            surveys.forEach(s -> surveyIdAndTitleDtoList.add(survey2activeSurveyDto(s)));
            surveyListResponse.setCount(surveys.size());
            surveyListResponse.setActiveSurveys(surveyIdAndTitleDtoList);
            return Optional.of(surveyListResponse);
        } else {
            return Optional.empty();
        }
    }

    public SurveyListResponse getCompletedSurveysByUserResponse(final CompletedSurveysRequest completedSurveysRequest,
                                                                final Principal principal) {
        int id = principal == null ? completedSurveysRequest.getAnonymousId()
                : userRepository.findByName(principal.getName()).get().getId();
        List<SurveysCompletedByUserNative> completedSurveys = surveyRepository.findAllSurveysCompletedByUser(id);

        return new SurveyListResponse(completedSurveys.size(), NativeSurveys2SurveysIdAndTitleList(completedSurveys));
    }

    public QuestionsAndAnswersResponse getQuestionsAndAnswersBySurveyByUserResponse(final QuestionsAndAnswersRequest questionsAndAnswersRequest,
                                                                                    final Principal principal) {
        int id = principal == null ? questionsAndAnswersRequest.getAnonymousId()
                : userRepository.findByName(principal.getName()).get().getId();
        List<QuestionsAndAnswersBySurveyByUserNative> questionsAndAnswersNative =
                surveyRepository.findQuestionsAndAnswersBySurveyByUser(id, questionsAndAnswersRequest.getSurveyId());

        return new QuestionsAndAnswersResponse(questionsAndAnswersRequest.getSurveyId(),
                questionsAndAnswersNative.size(),
                questionAndAnswerNative2questionsAndAnswersDto(questionsAndAnswersNative));
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

    private SurveyIdAndTitleDto survey2activeSurveyDto(final Survey survey) {
        SurveyIdAndTitleDto surveyIdAndTitleDto = new SurveyIdAndTitleDto();
        surveyIdAndTitleDto.setId(survey.getId());
        surveyIdAndTitleDto.setTitle(survey.getTitle());
        return surveyIdAndTitleDto;
    }

    private List<SurveyIdAndTitleDto> NativeSurveys2SurveysIdAndTitleList(
            final List<SurveysCompletedByUserNative> completedSurveys) {
        List<SurveyIdAndTitleDto> surveysList = new ArrayList<>();
        for (SurveysCompletedByUserNative nativeSurvey : completedSurveys) {
            SurveyIdAndTitleDto surveyIdAndTitleDto = new SurveyIdAndTitleDto();
            surveyIdAndTitleDto.setId(nativeSurvey.getId());
            surveyIdAndTitleDto.setTitle(nativeSurvey.getTitle());
            surveysList.add(surveyIdAndTitleDto);
        }
        return surveysList;
    }

    private List<QuestionAndAnswerDto> questionAndAnswerNative2questionsAndAnswersDto(
            final List<QuestionsAndAnswersBySurveyByUserNative> questionsAndAnswersNative) {
        List<QuestionAndAnswerDto> questionAndAnswerDtoList = new ArrayList<>();
        for (QuestionsAndAnswersBySurveyByUserNative qaNative : questionsAndAnswersNative) {
            QuestionAndAnswerDto questionAndAnswerDto = new QuestionAndAnswerDto();
            questionAndAnswerDto.setQuestionText(qaNative.getQuestion());
            String type = qaNative.getType();
            questionAndAnswerDto.setQuestionType(type);
            List<Integer> items;

            switch (type) {
                case "TEXT":
                    questionAndAnswerDto.setAnswerText(qaNative.getAnswer());
                    break;
                case "SINGLE_CHOICE":
                    items = new ArrayList<>();
                    items.add(Integer.parseInt(qaNative.getAnswer()));
                    questionAndAnswerDto.setItems(items);
                    break;
                case "MULTIPLE_CHOICE":
                    items = new ArrayList<>();
                    String itemsText = qaNative.getAnswer();
                    for (int i = 0; i < itemsText.length(); i++) {
                        items.add(Integer.parseInt(String.valueOf(itemsText.charAt(i))));
                    }
                    questionAndAnswerDto.setItems(items);
            }
            questionAndAnswerDtoList.add(questionAndAnswerDto);
        }
        return questionAndAnswerDtoList;
    }
}
