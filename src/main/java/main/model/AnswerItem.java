package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "answer_items")
@Data
@AllArgsConstructor
public class AnswerItem extends BaseEntity {
    @Column(name = "item_number")
    private int itemNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="answer_id")
    private Answer answer;

}
