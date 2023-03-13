package saessak.log.post.dto;

import lombok.Getter;

@Getter
public class SubscribePostDto {
    private long postId;
    private String profileId;
    private String imageFile;
    private long commentCount;
    private long reactionCount;
    private boolean like;

    public SubscribePostDto(long postId, String profileId, String imageFile, long commentCount, long reactionCount, boolean like) {
        this.postId = postId;
        this.profileId = profileId;
        this.imageFile = "https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + imageFile;
        this.commentCount = commentCount;
        this.reactionCount = reactionCount;
        this.like = like;
    }
}
