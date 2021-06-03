package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.QuestionAndAnswerDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsAndAnswersResponse {
    @JsonProperty("survey_id")
    private int surveyId;

    private int count;

    @JsonProperty("questions_and_answers")
    private List<QuestionAndAnswerDto> questionsAndAnswers;
}
