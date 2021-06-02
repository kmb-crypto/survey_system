package main.service;

import main.api.request.SurveyCreateRequest;
import main.api.response.SurveyCreateResponse;
import main.api.response.SurveyDeleteResponse;
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

    public SurveyCreateResponse getSurveyCreateResponse(final SurveyCreateRequest surveyCreateRequest) {
        Optional<HashMap<String, String>> optionalErrors = checkSurveyCreateRequest(surveyCreateRequest);
        if (optionalErrors.isPresent()) {
            return new SurveyCreateResponse(false, optionalErrors.get());
        } else {
            addNewSurvey(surveyCreateRequest);
            return new SurveyCreateResponse(true);
        }
    }

    //TODO изменение опроса

    public SurveyDeleteResponse getSurveyDeleteResponse(final int id) {
        if (surveyRepository.existsById(id)) {
            deleteSurvey(id);
            return new SurveyDeleteResponse(true);
        } else {
            return new SurveyDeleteResponse(false);
        }
    }

    private Optional<HashMap<String, String>> checkSurveyCreateRequest(final SurveyCreateRequest surveyCreateRequest) {
        HashMap<String, String> errors = new HashMap<>();
        if (surveyCreateRequest.getTitle().length() < minTitleLength) {
            errors.put("title", "Заголовок короче " + minTitleLength + " символов");
        }
        return errors.size() > 0 ? Optional.of(errors) : Optional.empty();
    }

    private void addNewSurvey(final SurveyCreateRequest surveyCreateRequest) {
        Survey survey = new Survey();
        survey.setTitle(surveyCreateRequest.getTitle());
        survey.setStartDate(surveyCreateRequest.getStartDate());
        survey.setFinishDate(surveyCreateRequest.getFinishDate());
        survey.setDescription(surveyCreateRequest.getDescription());
        survey.setActive(false);
        surveyRepository.save(survey);
    }

    private void deleteSurvey(final int id) {
        surveyRepository.deleteById(id);
    }
}
