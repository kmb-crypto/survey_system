package main.controller;

import main.api.request.CompletedSurveysRequest;
import main.api.request.QuestionsAndAnswersRequest;
import main.api.request.SurveyRequest;
import main.api.response.QuestionsAndAnswersResponse;
import main.api.response.SurveyListResponse;
import main.api.response.SurveyProcessResponse;
import main.api.response.SurveyResponse;
import main.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping(value = "/surveys/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable final int id) {
        Optional<SurveyResponse> optionalSurveyResponse = surveyService.getSurveyByIdResponse(id);
        if (optionalSurveyResponse.isPresent()) {
            return new ResponseEntity<SurveyResponse>(optionalSurveyResponse.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping(value = "/surveys/active")
    public ResponseEntity<SurveyListResponse> getActiveSurveys() {
        Optional<SurveyListResponse> optionalActiveSurveyResponse = surveyService.getActiveSurveyResponse();
        if (optionalActiveSurveyResponse.isPresent()) {
            return new ResponseEntity<SurveyListResponse>(optionalActiveSurveyResponse.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/surveys/complete")
    public ResponseEntity<SurveyListResponse> getCompletedSurveys(
            @RequestBody final CompletedSurveysRequest completedSurveysRequest, final Principal principal) {
        return new ResponseEntity<SurveyListResponse>(
                surveyService.getCompletedSurveysByUserResponse(completedSurveysRequest, principal), HttpStatus.OK);

    }

    @GetMapping(value = "/surveys/questions_and_answers")
    public ResponseEntity<QuestionsAndAnswersResponse> getQuestionsAndAnswers(
            @RequestBody final QuestionsAndAnswersRequest questionsAndAnswersRequest, final Principal principal) {
        return new ResponseEntity<QuestionsAndAnswersResponse>(
                surveyService.getQuestionsAndAnswersBySurveyByUserResponse(questionsAndAnswersRequest, principal), HttpStatus.OK);
    }

    @PostMapping(value = "/surveys")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyProcessResponse> createSurvey(
            @RequestBody final SurveyRequest surveyRequest) {
        return new ResponseEntity<SurveyProcessResponse>(surveyService.getSurveyCreateResponse(surveyRequest),
                HttpStatus.OK);
    }

    @PutMapping(value = "/surveys/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyProcessResponse> editSurvey(@PathVariable final int id,
                                                            @RequestBody final SurveyRequest surveyRequest) {
        SurveyProcessResponse response = surveyService.getSurveyEditResponse(id, surveyRequest);
        if (response.isResult()) {
            return new ResponseEntity<SurveyProcessResponse>(response, HttpStatus.OK);
        } else if (response.getErrors() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return new ResponseEntity<SurveyProcessResponse>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/surveys/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyProcessResponse> deleteSurvey(@PathVariable final int id) {
        SurveyProcessResponse response = surveyService.getSurveyDeleteResponse(id);
        if (response.isResult()) {
            return new ResponseEntity<SurveyProcessResponse>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
