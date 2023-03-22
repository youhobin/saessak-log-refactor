package saessak.log.reaction.Controller;


import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import saessak.log.reaction.service.ReactionService;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReactionController {

    private final ReactionService reactionService;

    @ApiResponse(code = 200, message = "좋아요 추가")
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Long> like(@PathVariable(value = "postId") Long postId,
                           Authentication authentication) {
        return ResponseEntity.ok().body(reactionService.likePost(postId, authentication.getName()));
    }

    @ApiResponse(code = 200, message = "좋아요 해제")
    @DeleteMapping("/posts/{postId}/unlike")
    public Object unlike(@PathVariable(value = "postId") Long postId,
                           Authentication authentication) {
        reactionService.unlikePost(postId, authentication.getName());
        return ResponseEntity.ok();
    }

}
