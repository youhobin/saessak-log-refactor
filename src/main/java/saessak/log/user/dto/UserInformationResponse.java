package saessak.log.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserInformationResponse {

    private Long userId;
    private String profileId;
    private String email;
    private String name;
    private List<Long> reactionPostId = new ArrayList<>();
    private List<Long> subscriptionToUserId = new ArrayList<>();

}
