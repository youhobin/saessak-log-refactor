package saessak.log.post_media.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saessak.log.post.Post;
import saessak.log.post_media.PostMedia;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMediaSaveDto {

    private String imageFile;

    private String postText;

    public PostMediaSaveDto(String imageFile, String postText) {
        this.imageFile = imageFile;
        this.postText = postText;
    }

    public PostMedia toEntity() {
        return PostMedia.of(imageFile, postText);
    }
}
