package main.controller;

import main.api.request.QuestionAddRequest;
import main.api.response.QuestionAddResponse;
import main.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/questions")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<QuestionAddResponse> addQuestion(@RequestBody final QuestionAddRequest questionAddRequest) {
        return new ResponseEntity<QuestionAddResponse>(questionService.getQuestionAddResponse(questionAddRequest), HttpStatus.OK);
    }
}
