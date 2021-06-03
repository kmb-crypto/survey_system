package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndAnswerDto {
    @JsonProperty("question_text")
    private String questionText;

    @JsonProperty("question_type")
    private String questionType;

    @JsonProperty("answer_text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answerText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Integer> items;
}
