package saessak.log.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseUserInformationDto {

    private Long userId;
    private String profileId;
    private String email;
    private String name;
    private List<Long> reactionPostId = new ArrayList<>();
    private List<Long> subscriptionToUserId = new ArrayList<>();

}
