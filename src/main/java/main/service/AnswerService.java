package main.service;

import main.api.request.AnswerRequest;
import main.api.response.AnswerResponse;
import main.model.Answer;
import main.model.Question;
import main.model.User;
import main.repository.AnswerRepository;
import main.repository.QuestionRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(final AnswerRepository answerRepository,
                         final UserRepository userRepository, final QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    public AnswerResponse getAnswerAddResponse(final AnswerRequest answerRequest, Principal principal) {
        Optional<User> optionalUser = principal == null ? userRepository.findById(answerRequest.getAnonymousId())
                : userRepository.findByName(principal.getName());

        Optional<Question> optionalQuestion = questionRepository.findById(answerRequest.getQuestionId());

        if (optionalUser.isEmpty() || optionalQuestion.isEmpty()) {
            return new AnswerResponse(false);
        } else {
            addAnswer(optionalUser.get(), optionalQuestion.get(), answerRequest);
            return new AnswerResponse(true);
        }

    }

    private void addAnswer(final User user, final Question question, final AnswerRequest answerRequest) {
        Answer answer = new Answer();
        answer.setUser(user);
        answer.setQuestion(question);

        switch (question.getQuestionType()) {
            case TEXT -> answer.setText(answerRequest.getText());
            case SINGLE_CHOICE -> {
                answer.setText(answerRequest.getItems().get(0).toString());
            }
            case MULTIPLE_CHOICE -> {
                String choiceText = "";
                for (Integer item : answerRequest.getItems()) {
                    choiceText = choiceText.concat(item.toString());
                }
                answer.setText(choiceText);
            }
        }
        answerRepository.save(answer);
    }
}
