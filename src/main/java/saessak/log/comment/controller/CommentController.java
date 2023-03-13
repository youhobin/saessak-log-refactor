package saessak.log.comment.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saessak.log.comment.Comment;
import saessak.log.comment.dto.CommentSaveDto;
import saessak.log.comment.dto.CommentSaveResponseDto;
import saessak.log.comment.dto.CommentViewDto;
import saessak.log.comment.service.CommentService;
import saessak.log.user.User;
import saessak.log.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    @ApiOperation(value = "댓글 작성")
    @ApiResponse(code = 200, message = "댓글을 작성하셨습니다.", response = CommentSaveResponseDto.class)
    @PostMapping("/comment")
    public ResponseEntity<CommentSaveResponseDto> saveComment(@RequestBody CommentSaveDto commentSaveDto, Authentication authentication) {
        String userProfileId = authentication.getName();
        Long savedComment = commentService.saveComment(commentSaveDto, userProfileId);
        CommentSaveResponseDto commentSaveResponseDto = new CommentSaveResponseDto(savedComment);
        return ResponseEntity.ok().body(commentSaveResponseDto);
    }

    @ApiOperation(value = "댓글 페이징")
    @ApiResponse(code = 200, message = "", response = CommentViewDto.class, responseContainer = "list")
    @GetMapping("/comment/{post}")
    public ResponseEntity<List<CommentViewDto>> fetchComment(@PathVariable(value = "post") Long post,
                                                             @RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "page", required = false) Integer page
    ) {
        List<CommentViewDto> list = commentService.fetchComments(post, limit, page);
        return ResponseEntity.ok().body(list);

    }
}
