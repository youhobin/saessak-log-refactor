package saessak.log.comment;

import lombok.*;
import saessak.log.BaseTimeEntity;
import saessak.log.post.Post;
import saessak.log.user.User;


import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post post;

    private String comment;

    @Builder
    private Comment(User user, Post post, String comment) {
        this.user = user;
        this.post = post;
        this.comment = comment;
    }

    public static Comment of(User user, Post post, String comment) {
        return Comment.builder()
            .user(user)
            .post(post)
            .comment(comment)
            .build();
    }

    //연관관계 메서드
    public void belongToPost(Post post) {
        this.post = post;
        post.createComments(this);
    }

}
