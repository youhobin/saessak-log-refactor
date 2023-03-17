package saessak.log.comment.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import saessak.log.comment.dto.AllCommentsByPostResponse;
import saessak.log.comment.dto.CommentSaveDto;
import saessak.log.comment.dto.CommentSaveResponseDto;
import saessak.log.comment.dto.CommentResponse;
import saessak.log.comment.service.CommentService;
import saessak.log.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @ApiResponse(code = 200, message = "댓글을 작성하셨습니다.", response = CommentSaveResponseDto.class)
    @PostMapping("/{postId}/comments/new")
    public ResponseEntity<CommentSaveResponseDto> saveComment(@RequestBody CommentSaveDto commentSaveDto,
                                                              @PathVariable("postId") Long postId,
                                                              Authentication authentication) {
        String userProfileId = authentication.getName();
        Long savedCommentId = commentService.saveComment(commentSaveDto, postId, userProfileId);
        CommentSaveResponseDto commentSaveResponseDto = new CommentSaveResponseDto(savedCommentId);
        return ResponseEntity.ok().body(commentSaveResponseDto);
    }

    @ApiOperation(value = "댓글 페이징")
    @ApiResponse(code = 200, message = "", response = CommentResponse.class, responseContainer = "list")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<AllCommentsByPostResponse> fetchComment(@PathVariable(value = "postId") Long postId,
                                                              @RequestParam(value = "limit", required = false, defaultValue = "6") Integer limit,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {

        AllCommentsByPostResponse commentResponse = commentService.fetchComments(postId, limit, page);
        return ResponseEntity.ok().body(commentResponse);
    }
}
