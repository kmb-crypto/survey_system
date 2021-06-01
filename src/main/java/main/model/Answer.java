package main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class Answer extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type",  nullable = false, columnDefinition = "enum('TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE')")
    protected QuestionType questionType;
}
