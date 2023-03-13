package saessak.log.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.dto.SubscriptionDto;
import saessak.log.subscription.repository.SubscriptionRepository;
import saessak.log.user.User;
import saessak.log.user.repository.UserRepository;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscriptionService {

    final UserRepository userRepository;
    final PostRepository postRepository;
    final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Boolean subscribe(String fromUserProfileId, Long toUserId) {
        User fromUser = userRepository.findByProfileId(fromUserProfileId);
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new IllegalArgumentException());
        Subscription previousSubscription = subscriptionRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId);
        if (previousSubscription == null) {
            Subscription subscription = new Subscription();
            subscription.setToUserId(toUser);
            log.info("toUser setting done");
            subscription.setFromUserId(fromUser);
            log.info("setting done");
            subscriptionRepository.save(subscription);
            return true;
        } else {
            subscriptionRepository.deleteById(previousSubscription.getId());
            return false;
        }
    }

}
