package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.QuestionDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponse {
    private int id;
    private int count;

    private List<QuestionDto> questions;
}
