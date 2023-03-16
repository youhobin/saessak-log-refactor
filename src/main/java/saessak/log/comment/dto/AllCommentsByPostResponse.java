package saessak.log.comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllCommentsByPostResponse {

    List<CommentResponse> commentResponseList;

    public AllCommentsByPostResponse(List<CommentResponse> commentResponseList) {
        this.commentResponseList = commentResponseList;
    }
}
