package main.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "surveys")
@NoArgsConstructor
@Data
public class Survey extends BaseEntity {
    private String title;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "finish_date")
    private Date finishDate;

    private String description;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
