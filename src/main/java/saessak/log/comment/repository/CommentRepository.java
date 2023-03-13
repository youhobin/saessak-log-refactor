package saessak.log.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.comment.Comment;
import saessak.log.comment.dto.CommentViewDto;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new saessak.log.comment.dto.CommentViewDto(A.id, A.user.profileId, A.comment, A.createdDate) "
            + "FROM Comment A "
            + "WHERE A.post.id = :postId")
    List<CommentViewDto> commentViewDto(@Param("postId") Long postId, Pageable pageable);

}