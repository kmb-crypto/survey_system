package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data@NoArgsConstructor
public class User extends BaseEntity{
    @Column(nullable = false)
    private String name;

    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    @Column(nullable = false)
    private String password;

}
