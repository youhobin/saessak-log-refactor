package saessak.log.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.comment.Comment;
import saessak.log.comment.dto.CommentResponse;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c from Comment c " +
        "join fetch c.user u " +
        "where c.post.id = :postId",
        countQuery = "select count(c) from Comment c")
    Page<Comment> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

}