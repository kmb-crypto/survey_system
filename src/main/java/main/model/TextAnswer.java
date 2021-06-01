package main.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name="text_answers")
public class TextAnswer extends Answer {

    private String text;

    public TextAnswer() {
        questionType = QuestionType.TEXT;
    }

}
