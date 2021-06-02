package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "answers")
@Data
public class Answer extends BaseEntity {

    private String text;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AnswerItem> answerItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
