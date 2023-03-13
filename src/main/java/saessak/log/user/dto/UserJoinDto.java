package saessak.log.user.dto;

import lombok.*;
import saessak.log.user.User;

@Getter
@Setter
public class UserJoinDto {

    private String profileId;
    private String password;
    private String passwordCheck;
    private String name;
    private String email;

    public User toEntity() {
        return User.builder()
                .profileId(profileId)
                .password(password)
                .email(email)
                .name(name).build();

    }
}
