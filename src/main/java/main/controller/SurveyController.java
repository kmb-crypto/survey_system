package main.controller;

import main.api.request.SurveyCreateRequest;
import main.api.response.SurveyCreateResponse;
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
    public ResponseEntity<SurveyCreateResponse> createSurvey(
            @RequestBody final SurveyCreateRequest surveyCreateRequest) {
        return new ResponseEntity<SurveyCreateResponse>(surveyService.getSurveyCreateResponse(surveyCreateRequest),
                HttpStatus.OK);
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
