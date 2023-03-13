package saessak.log.user.dto;

import lombok.*;
import saessak.log.user.User;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String profileId;
    private String password;
    private String name;
    private String email;

    @Builder
    public UserDto(Long id, String profileId, String password, String name, String email) {
        this.id = id;
        this.profileId = profileId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return User.builder()
                .profileId(profileId)
                .password(password)
                .email(email)
                .name(name).build();

    }
}
