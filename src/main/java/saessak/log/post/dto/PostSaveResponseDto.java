package saessak.log.post.dto;

import lombok.Getter;
import lombok.Setter;
import saessak.log.post.Post;
import saessak.log.user.User;

@Getter
public class PostSaveResponseDto {

    private Long postId;

    public PostSaveResponseDto(Long postId) {
        this.postId = postId;
    }
}
