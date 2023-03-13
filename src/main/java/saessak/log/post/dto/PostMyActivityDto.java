package saessak.log.post.dto;

import lombok.Getter;

@Getter
public class PostMyActivityDto {

    private Long postId;
    private String imageFile;
    private long commentCount;
    private long reactionCount;

    public PostMyActivityDto(Long postId, String imageFile, long commentCount, long reactionCount) {
        this.postId = postId;
        this.imageFile = "https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + imageFile;
        this.commentCount = commentCount;
        this.reactionCount = reactionCount;
    }
}
