package saessak.log.comment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.comment.Comment;
import saessak.log.comment.dto.AllCommentsByPostResponse;
import saessak.log.comment.dto.CommentSaveDto;
import saessak.log.comment.dto.CommentResponse;
import saessak.log.comment.repository.CommentRepository;
import saessak.log.post.Post;
import saessak.log.post.repository.PostRepository;
import saessak.log.user.User;
import saessak.log.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    final CommentRepository commentRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;

    @Transactional
    public Long saveComment(CommentSaveDto commentSaveDto, Long postId, String userProfileId) {
        User user = userRepository.findByProfileId(userProfileId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException());
        String commentContent = commentSaveDto.getComment();

        Comment comment = Comment.of(user, post, commentContent);
        comment.belongToPost(post);
        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }

    public AllCommentsByPostResponse fetchComments(Long postId, int limit, int page) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Comment> commentsPage = commentRepository.findCommentsByPostId(postId, pageRequest);
        List<Comment> comments = commentsPage.getContent();

        List<CommentResponse> commentsResponse = comments.stream().map(CommentResponse::new).collect(Collectors.toList());
        return new AllCommentsByPostResponse(commentsResponse);

    }

}
