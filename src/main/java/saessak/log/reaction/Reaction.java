package saessak.log.reaction;

import lombok.*;
import saessak.log.post.Post;

import saessak.log.user.User;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reaction {

    @Id
    @GeneratedValue
    @Column(name = "reaction_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post post;

    @Builder
    private Reaction(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static Reaction of(User user, Post post) {
        return Reaction.builder()
            .user(user)
            .post(post)
            .build();
    }
}
