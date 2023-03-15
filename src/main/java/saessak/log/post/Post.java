package saessak.log.post;

import lombok.*;
import saessak.log.BaseTimeEntity;

import saessak.log.comment.Comment;
import saessak.log.post_media.PostMedia;
import saessak.log.reaction.Reaction;
import saessak.log.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;


    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY)
    private PostMedia postMedia;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Reaction> reactions = new ArrayList<>();

    private Post(User user) {
        this.user = user;
    }

    public static Post from(User user) {
        return new Post(user);
    }

    //연관관계 편의 메서드/
    public void createPostMedia(PostMedia postMedia) {
        this.postMedia = postMedia;
    }

    public void createComments(Comment comment) {
        this.comments.add(comment);
    }
}
