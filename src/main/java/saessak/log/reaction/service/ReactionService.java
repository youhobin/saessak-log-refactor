package saessak.log.reaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.reaction.Reaction;
import saessak.log.reaction.dto.ReactionDto;
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
    public boolean reaction(Long postId, String userProfileId) {
        User user = userRepository.findByProfileId(userProfileId);
        Reaction previousReaction = reactionRepository.findByUserIdxAndPostIdx(user.getId(), postId);
        if (previousReaction == null) {
            Reaction reaction = new Reaction();
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException());
            reaction.setUser(user);
            reaction.setPost(post);
            reactionRepository.save(reaction);
            return true;
        } else {
            reactionRepository.deleteById(previousReaction.getId());
            return false;
        }
    }
}
