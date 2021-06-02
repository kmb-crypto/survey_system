package main.service;

import main.api.request.AnswerRequest;
import main.api.response.AnswerResponse;
import main.model.Answer;
import main.model.AnswerItem;
import main.model.Question;
import main.model.User;
import main.repository.AnswerItemRepository;
import main.repository.AnswerRepository;
import main.repository.QuestionRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerItemRepository answerItemRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(final AnswerRepository answerRepository, final AnswerItemRepository answerItemRepository,
                         final UserRepository userRepository, final QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.answerItemRepository = answerItemRepository;
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
        answer.setText(answerRequest.getText());
        answerRepository.save(answer);
        List<AnswerItem> answerItems = new ArrayList<>();
        AnswerItem answerItem;
        switch (question.getQuestionType()) {
            case SINGLE_CHOICE -> {
                answerItem = new AnswerItem(answerRequest.getItems().get(0), answer);
                answerItemRepository.save(answerItem);
                answerItems.add(answerItem);
                answer.setAnswerItem(answerItems);
            }
            case MULTIPLE_CHOICE -> {
                for (Integer item : answerRequest.getItems()) {
                    answerItem = new AnswerItem(item, answer);
                    answerItemRepository.save(answerItem);
                }
                answer.setAnswerItem(answerItems);
            }
        }
    }
}
