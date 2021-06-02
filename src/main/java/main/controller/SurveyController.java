package main.controller;

import main.api.request.SurveyRequest;
import main.api.response.SurveyResponse;
import main.api.response.SurveyDeleteResponse;
import main.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping(value = "/surveys")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyResponse> createSurvey(
            @RequestBody final SurveyRequest surveyRequest) {
        return new ResponseEntity<SurveyResponse>(surveyService.getSurveyCreateResponse(surveyRequest),
                HttpStatus.OK);
    }

    @PutMapping(value = "/surveys/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyResponse> editSurvey(@PathVariable final int id,
                                                     @RequestBody final SurveyRequest surveyRequest) {
        SurveyResponse response = surveyService.getSurveyEditResponse(id, surveyRequest);
        if (response.isResult()) {
            return new ResponseEntity<SurveyResponse>(response, HttpStatus.OK);
        } else if (response.getErrors() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return new ResponseEntity<SurveyResponse>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/surveys/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyDeleteResponse> deleteSurvey(@PathVariable final int id) {
        SurveyDeleteResponse response = surveyService.getSurveyDeleteResponse(id);
        if (response.isResult()) {
            return new ResponseEntity<SurveyDeleteResponse>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
