package saessak.log.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SubscribePostResponse {

    List<SubscribePostDto> subscribePostList;

    public SubscribePostResponse(List<SubscribePostDto> subscribePostList) {
        this.subscribePostList = subscribePostList;
    }
}
