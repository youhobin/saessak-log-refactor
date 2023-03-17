package saessak.log.subscription;

import lombok.*;
import saessak.log.user.User;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUserId;

    @Builder
    public Subscription(User toUserId, User fromUserId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }

    public static Subscription of(User toUserId, User fromUserId) {
        return Subscription.builder()
            .toUserId(toUserId)
            .fromUserId(fromUserId)
            .build();
    }

}
