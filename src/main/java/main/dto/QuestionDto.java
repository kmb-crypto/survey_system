package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private int id;
    private String text;
    private String type;

    @JsonProperty("amount_of_items")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amountOfItems;
}
