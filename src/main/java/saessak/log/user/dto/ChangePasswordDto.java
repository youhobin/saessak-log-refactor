package saessak.log.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordDto {
    private String password;
    private String passwordCheck;
}
