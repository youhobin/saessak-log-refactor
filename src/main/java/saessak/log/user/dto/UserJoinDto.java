package saessak.log.user.dto;

import lombok.*;
import saessak.log.user.User;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinDto {

    @NotNull
    private String profileId;

    @NotNull
    private String password;

    @NotNull
    private String passwordCheck;

    @NotNull
    private String name;

    @NotNull
    private String email;

    public User toEntity() {
        return User.builder()
                .profileId(profileId)
                .password(password)
                .email(email)
                .name(name).build();

    }
}
