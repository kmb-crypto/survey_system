package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
public class Question extends BaseEntity {
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, columnDefinition = "enum('TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE')")
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(name = "amount_of_items")
    private Integer amountOfItems;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}
