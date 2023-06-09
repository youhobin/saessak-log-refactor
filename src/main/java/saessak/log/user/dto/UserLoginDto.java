package saessak.log.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginDto {

    @NotNull
    private String profileId;

    @NotNull
    private String password;
}
