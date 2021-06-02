package main.controller;

import main.api.request.AnswerRequest;
import main.api.response.AnswerResponse;
import main.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(final AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping(value = "/answers")
    public ResponseEntity<AnswerResponse> postAnswer(@RequestBody final AnswerRequest answerRequest, Principal principal) {
        return new ResponseEntity<AnswerResponse>(answerService.getAnswerAddResponse(answerRequest, principal), HttpStatus.OK);
    }
}
