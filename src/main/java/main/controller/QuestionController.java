package main.controller;

import main.api.request.QuestionRequest;
import main.api.response.QuestionProcessResponse;
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
    public ResponseEntity<QuestionProcessResponse> addQuestion(@RequestBody final QuestionRequest questionRequest) {
        return new ResponseEntity<QuestionProcessResponse>(questionService.getQuestionAddResponse(questionRequest),
                HttpStatus.OK);
    }

    @PutMapping(value = "/questions/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<QuestionProcessResponse> editQuestion(@PathVariable final int id,
                                                                @RequestBody final QuestionRequest questionRequest) {
        return new ResponseEntity<QuestionProcessResponse>(questionService.getQuestionEditResponse(id, questionRequest),
                HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<QuestionProcessResponse> deleteQuestion(@PathVariable final int id) {
        QuestionProcessResponse response = questionService.getQuestionDeleteResponse(id);
        if (response.isResult()) {
            return new ResponseEntity<QuestionProcessResponse>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
