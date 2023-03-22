package saessak.log.user;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import saessak.log.reaction.Reaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_idx")
    private Long id;

    @Column(nullable = false)
    private String profileId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reaction> reactions = new ArrayList<>();

    @Builder
    private User(String profileId, String password, String name, String email) {
        this.profileId = profileId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = Role.USER;
    }

    public static User of(String profileId, String password, String name, String email) {
        return User.builder()
            .profileId(profileId)
            .password(password)
            .name(name)
            .email(email)
            .build();
    }

    public User passwordEncode(BCryptPasswordEncoder encoder) {
        this.password = encoder.encode(password);
        return this;
    }

    public void changeTempPassword(String resetPassword) {
        this.password = resetPassword;
    }
}
