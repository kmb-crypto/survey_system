package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.SurveyIdAndTitleDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyListResponse {
    private int count;
    @JsonProperty("surveys")
    private List<SurveyIdAndTitleDto> activeSurveys;
}
