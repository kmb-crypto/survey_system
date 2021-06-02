package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.QuestionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAddRequest {
    @JsonProperty("survey_id")
    private int surveyId;

    @JsonProperty("question_type")
    private QuestionType questionType;

    private String text;

    @JsonProperty("number_of_items")
    private Integer numberOfItems;
}
