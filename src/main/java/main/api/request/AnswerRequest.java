package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.QuestionType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequest {
    @JsonProperty("question_id")
    private int questionId;

    @JsonProperty("anonymous_id")
    private int anonymousId;

    private String text;
    private List<Integer> items;

    @JsonProperty("question_type")
    private QuestionType questionType;
}
