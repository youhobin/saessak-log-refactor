package saessak.log.reaction;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saessak.log.post.Post;

import saessak.log.user.User;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reaction {

    @Id
    @GeneratedValue
    @Column(name = "reaction_idx")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private Post post;
}
