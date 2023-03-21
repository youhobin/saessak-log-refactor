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

    private long reactionCount;

    private long commentsCount;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private PostMedia postMedia;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    private Post(User user) {
        this.user = user;
        this.reactionCount = 0;
        this.commentsCount = 0;
    }

    public static Post from(User user) {
        return new Post(user);
    }

    //연관관계 편의 메서드/
    public void createPostMedia(PostMedia postMedia) {
        this.postMedia = postMedia;
    }

    public void plusCommentsCount() {
        this.commentsCount = comments.size();
    }

    public void createComments(Comment comment) {
        this.comments.add(comment);
    }

    public void plusReactionCount() {
        this.reactionCount++;
    }

    public void minusReactionCount() {
        if (reactionCount <= 0) {
            throw new IllegalStateException("좋아요를 취소할 수 없습니다.");
        }
        this.reactionCount--;
    }
}
