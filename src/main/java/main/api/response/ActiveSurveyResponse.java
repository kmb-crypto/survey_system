package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.ActiveSurveyDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSurveyResponse {
    private int count;
    @JsonProperty("surveys")
    private List<ActiveSurveyDto> activeSurveys;
}
