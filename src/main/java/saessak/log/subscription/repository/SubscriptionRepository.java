package saessak.log.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.post.Post;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.dto.SubscriptionDto;
import saessak.log.user.User;

import java.util.List;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.fromUserId.id = :fromUserId and s.toUserId.id = :toUserId")
    Subscription findByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    @Query("select s from Subscription s" +
        " join fetch s.toUserId u" +
        " where s.fromUserId.id = :fromUserId")
    List<Subscription> findByFromUserID(@Param("fromUserId") Long fromUserId);
}