package saessak.log.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.subscription.Subscription;
import saessak.log.user.User;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s" +
        " join fetch s.toUserId u" +
        " where s.fromUserId.id = :fromUserId")
    List<Subscription> findByFromUserID(@Param("fromUserId") Long fromUserId);

    @Query("select s from Subscription s" +
        " join fetch s.fromUserId fu" +
        " join fetch s.toUserId tu" +
        " where fu.profileId = :fromUserProfileId" +
        " and tu.id = :toUserId")
    Subscription findByFromUserProfileIdAndToUserId(@Param("fromUserProfileId") String fromUserProfileId, @Param("toUserId") Long toUserId);
}