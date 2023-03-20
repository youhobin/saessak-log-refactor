package saessak.log.reaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.reaction.Reaction;
import saessak.log.reaction.repository.ReactionRepository;
import saessak.log.user.User;
import saessak.log.user.repository.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReactionService {

    final PostRepository postRepository;
    final UserRepository userRepository;
    final ReactionRepository reactionRepository;

    @Transactional
    public Long likePost(Long postId, String profileId) {
        User user = userRepository.findByProfileId(profileId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("게시글을 찾을 수 없습니다."));
        post.plusReactionCount();

        Reaction reaction = Reaction.of(user, post);
        Reaction savedReaction = reactionRepository.save(reaction);
        return savedReaction.getId();
    }

    @Transactional
    public void unlikePost(Long postId, String profileId) {
        Reaction reaction = reactionRepository.findReaction(postId, profileId).orElseThrow(() -> new IllegalStateException());
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("게시글을 찾을 수 없습니다."));
        post.minusReactionCount();

        reactionRepository.deleteById(reaction.getId());
    }
}
