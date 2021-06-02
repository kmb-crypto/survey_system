package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(name = "is_moderator", nullable = false)
    private boolean isModerator;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public Role getRole() {
        return isModerator ? Role.MODERATOR : Role.USER;
    }
}
