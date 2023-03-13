package saessak.log.comment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.comment.Comment;
import saessak.log.comment.dto.CommentSaveDto;
import saessak.log.comment.dto.CommentViewDto;
import saessak.log.comment.repository.CommentRepository;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.user.User;
import saessak.log.user.repository.UserRepository;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    final CommentRepository commentRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;

    @Transactional
    public Long saveComment(CommentSaveDto commentSaveDto, String userProfileId) {
        User user = userRepository.findByProfileId(userProfileId);
        Post post = postRepository.findById(commentSaveDto.getPost()).orElseThrow(() -> new IllegalArgumentException());
        Comment comment = new Comment();
        comment.setComment(commentSaveDto.getComment());
        comment.setUser(user);
        comment.belongToPost(post);
        comment = commentRepository.save(comment);

        return comment.getId();
    }

    public List<CommentViewDto> fetchComments(Long postId, int limit, int page) {
        return commentRepository.commentViewDto(postId, PageRequest.of(page, limit));
    }

}
