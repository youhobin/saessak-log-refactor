package saessak.log.comment.dto;


import lombok.Getter;

@Getter
public class CommentSaveResponseDto {

    private Long commentId;

    public CommentSaveResponseDto(Long commentId) {
        this.commentId = commentId;
    }
}
