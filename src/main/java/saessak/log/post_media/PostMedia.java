package saessak.log.post_media;

import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saessak.log.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostMedia {

    @Id
    @GeneratedValue
    @Column(name = "media_idx")
    private Long id;

    private String imageFile;

    private String postText;


    @Builder
    private PostMedia(String imageFile, String postText) {
        this.imageFile = imageFile;
        this.postText = postText;
    }

    public static PostMedia of(String imageFile, String postText) {
        return PostMedia.builder()
            .imageFile(imageFile)
            .postText(postText)
            .build();
    }

}
