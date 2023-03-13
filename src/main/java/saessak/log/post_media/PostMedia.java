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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post post;

    private String imageFile;

    private String postText;


    @Builder
    private PostMedia(Post post, String imageFile, String postText) {
        this.post = post;
        this.imageFile = imageFile;
        this.postText = postText;
    }

    public static PostMedia of(String imageFile, String postText) {
        return PostMedia.builder()
            .imageFile(imageFile)
            .postText(postText)
            .build();
    }

    //연관관계 편의 메서드/
    public void belongToPost(Post post) {
        this.post = post;
        post.createPostMedia(this);
    }

}
