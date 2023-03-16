package saessak.log.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import saessak.log.comment.Comment;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long commentId;
    private String profileId;
    private String comment;
    private LocalDateTime createdDate;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.profileId = comment.getUser().getProfileId();
        this.comment = comment.getComment();
        this.createdDate = comment.getCreatedDate();
    }
}
