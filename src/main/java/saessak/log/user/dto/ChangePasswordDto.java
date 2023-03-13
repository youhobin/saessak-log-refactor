package saessak.log.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private String password;
    private String passwordCheck;
}
