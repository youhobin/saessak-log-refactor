package saessak.log.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.dto.SubscriptionResponse;
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
    public SubscriptionResponse subscribeUser(String fromUserProfileId, Long postId) {
        Post post = postRepository.findWithUserById(postId);
        User toUser = post.getUser();
        User fromUser = userRepository.findByProfileId(fromUserProfileId);

        Subscription subscription = Subscription.of(toUser, fromUser);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return new SubscriptionResponse(savedSubscription.getId(), true);
    }

    @Transactional
    public void unSubscribeUser(String fromUserProfileId, Long postId) {
        Post post = postRepository.findWithUserById(postId);
        Long toUserId = post.getUser().getId();

        Subscription findSubscription = subscriptionRepository.findByFromUserProfileIdAndToUserId(fromUserProfileId, toUserId);
        subscriptionRepository.deleteById(findSubscription.getId());
    }
}
