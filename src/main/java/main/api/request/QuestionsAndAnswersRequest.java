package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsAndAnswersRequest {
    @JsonProperty("survey_id")
    private int surveyId;

    @JsonProperty("anonymous_id")
    private int anonymousId;
}
