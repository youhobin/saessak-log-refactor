package saessak.log.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MyActivitiesResponse {

    List<PostMyActivityDto> postMyActivityDtoList;

    public MyActivitiesResponse(List<PostMyActivityDto> postMyActivityDtoList) {
        this.postMyActivityDtoList = postMyActivityDtoList;
    }
}
