package saessak.log.post.dto;

import lombok.Getter;
import lombok.Setter;
import saessak.log.comment.Comment;
import saessak.log.post.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {

    private String profileId;
    private String imageFile;
    private String postText;
    private long likeCount;
    private boolean subscribe;
    private boolean like;

    public PostResponseDto(String profileId, String imageFile, String postText, long likeCount) {
        this(profileId, imageFile, postText, likeCount, false, false);
    }

    public PostResponseDto(String profileId, String imageFile, String postText, long likeCount, boolean subscribe, boolean like) {
        this.profileId = profileId;
        this.imageFile = "https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + imageFile;
        this.postText = postText;
        this.likeCount = likeCount;
        this.subscribe = subscribe;
        this.like = like;
    }
}
