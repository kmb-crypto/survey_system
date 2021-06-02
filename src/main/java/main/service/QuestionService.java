package main.service;

import main.api.request.QuestionRequest;
import main.api.response.QuestionResponse;
import main.model.Question;
import main.model.QuestionType;
import main.model.Survey;
import main.repository.QuestionRepository;
import main.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public QuestionService(final QuestionRepository questionRepository, final SurveyRepository surveyRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    public QuestionResponse getQuestionAddResponse(final QuestionRequest questionRequest) {
        if (checkSurveyForQuestion(questionRequest.getSurveyId())) {
            addQuestion(questionRequest);
            return new QuestionResponse(true);
        } else {
            return new QuestionResponse(false);
        }
    }

    //TODO изменение вопроса

    public QuestionResponse getQuestionDeleteResponse(final int id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return new QuestionResponse(true);
        } else {
            return new QuestionResponse(false);
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
            question.setNumberOfItems(questionRequest.getNumberOfItems());
        }
        questionRepository.save(question);
    }

    private boolean checkSurveyForQuestion(final int id) {
        return surveyRepository.existsById(id);
    }

}
