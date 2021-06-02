package main.controller;

import main.api.request.SurveyCreateRequest;
import main.api.response.SurveyCreateResponse;
import main.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping(value = "/surveys")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<SurveyCreateResponse> createSurvey(@RequestBody final SurveyCreateRequest surveyCreateRequest,
                                                             final Principal principal) {
        return new ResponseEntity<SurveyCreateResponse>(surveyService.createSurvey(surveyCreateRequest),
                HttpStatus.OK);
    }
}
