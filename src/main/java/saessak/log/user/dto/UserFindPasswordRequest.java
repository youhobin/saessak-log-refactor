package saessak.log.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPasswordRequest {
    private String name;
    private String profileId;
    private String email;

}
