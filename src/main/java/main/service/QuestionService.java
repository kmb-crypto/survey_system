package main.service;

import main.api.request.QuestionRequest;
import main.api.response.QuestionProcessResponse;
import main.model.Question;
import main.model.QuestionType;
import main.model.Survey;
import main.repository.QuestionRepository;
import main.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public QuestionService(final QuestionRepository questionRepository, final SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    public QuestionProcessResponse getQuestionAddResponse(final QuestionRequest questionRequest) {
        if (checkSurveyForQuestion(questionRequest.getSurveyId())) {
            addQuestion(questionRequest);
            return new QuestionProcessResponse(true);
        } else {
            return new QuestionProcessResponse(false);
        }
    }

    public QuestionProcessResponse getQuestionEditResponse(final int id, final QuestionRequest questionRequest) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            setEditableQuestion(optionalQuestion.get(), questionRequest);
            return new QuestionProcessResponse(true);
        } else {
            return new QuestionProcessResponse(false);
        }
    }

    public QuestionProcessResponse getQuestionDeleteResponse(final int id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return new QuestionProcessResponse(true);
        } else {
            return new QuestionProcessResponse(false);
        }
    }

    private void addQuestion(final QuestionRequest questionRequest) {
        Question question = new Question();
        QuestionType type = questionRequest.getQuestionType();
        Survey survey = surveyRepository.findById(questionRequest.getSurveyId()).get();
        question.setSurvey(survey);
        question.setQuestionType(type);
        question.setText(questionRequest.getText());
        if (type.equals(QuestionType.SINGLE_CHOICE) || type.equals(QuestionType.MULTIPLE_CHOICE)) {
            question.setAmountOfItems(questionRequest.getNumberOfItems());
        }
        questionRepository.save(question);
    }

    private void setEditableQuestion(final Question question, final QuestionRequest questionRequest) {
        question.setText(questionRequest.getText());
        question.setQuestionType(questionRequest.getQuestionType());
        question.setAmountOfItems(questionRequest.getNumberOfItems());
        questionRepository.save(question);
    }

    private boolean checkSurveyForQuestion(final int id) {
        return surveyRepository.existsById(id);
    }

}
