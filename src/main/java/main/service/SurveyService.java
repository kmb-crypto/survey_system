package main.service;

import main.api.request.SurveyRequest;
import main.api.response.SurveyResponse;
import main.model.Survey;
import main.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public SurveyResponse getSurveyCreateResponse(final SurveyRequest surveyRequest) {
        Optional<HashMap<String, String>> optionalErrors = checkSurveyRequest(surveyRequest);
        if (optionalErrors.isPresent()) {
            return new SurveyResponse(false, optionalErrors.get());
        } else {
            addNewSurvey(surveyRequest);
            return new SurveyResponse(true);
        }
    }

    public SurveyResponse getSurveyEditResponse(final int id, final SurveyRequest surveyRequest) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Optional<HashMap<String, String>> optionalErrors = checkSurveyRequest(surveyRequest);
            if (optionalErrors.isPresent()) {
                return new SurveyResponse(false, optionalErrors.get());
            } else {
                setEditableSurvey(optionalSurvey.get(), surveyRequest);
                return new SurveyResponse(true);
            }
        } else {
            return new SurveyResponse(false);
        }
    }

    public SurveyResponse getSurveyDeleteResponse(final int id) {
        if (surveyRepository.existsById(id)) {
            surveyRepository.deleteById(id);
            return new SurveyResponse(true);
        } else {
            return new SurveyResponse(false);
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
        survey.setActive(false);
        surveyRepository.save(survey);
    }

    private void setEditableSurvey(final Survey survey, final SurveyRequest surveyRequest) {
        survey.setFinishDate(surveyRequest.getFinishDate());
        survey.setTitle(surveyRequest.getTitle());
        survey.setDescription(surveyRequest.getDescription());
        survey.setActive(surveyRequest.getActive());
        surveyRepository.save(survey);
    }

}
