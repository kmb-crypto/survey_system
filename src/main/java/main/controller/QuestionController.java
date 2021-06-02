package main.controller;

import main.api.request.QuestionRequest;
import main.api.response.QuestionResponse;
import main.api.response.SurveyResponse;
import main.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/questions")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<QuestionResponse> addQuestion(@RequestBody final QuestionRequest questionRequest) {
        return new ResponseEntity<QuestionResponse>(questionService.getQuestionAddResponse(questionRequest), HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<QuestionResponse> deleteQuestion(@PathVariable final int id) {
        QuestionResponse response = questionService.getQuestionDeleteResponse(id);
        if (response.isResult()) {
            return new ResponseEntity<QuestionResponse>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
