package main.service;

import main.api.request.QuestionAddRequest;
import main.api.response.QuestionAddResponse;
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

    public QuestionAddResponse getQuestionAddResponse(final QuestionAddRequest questionAddRequest) {
        int surveyId = questionAddRequest.getSurveyId();
        if (surveyRepository.existsById(surveyId)) {
            addQuestion(questionAddRequest);
            return new QuestionAddResponse(true);
        } else {
            return new QuestionAddResponse(false);
        }

    }

    //TODO изменение вопроса

    //TODO удаление вопроса

    private void addQuestion(final QuestionAddRequest questionAddRequest) {
        Question question = new Question();
        QuestionType type = questionAddRequest.getQuestionType();
        Survey survey = surveyRepository.findById(questionAddRequest.getSurveyId()).get();
        question.setSurvey(survey);
        question.setQuestionType(type);
        question.setText(questionAddRequest.getText());
        if (type.equals(QuestionType.SINGLE_CHOICE) || type.equals(QuestionType.MULTIPLE_CHOICE)) {
            question.setNumberOfItems(questionAddRequest.getNumberOfItems());
        }
        questionRepository.save(question);
    }
}
