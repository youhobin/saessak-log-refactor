package saessak.log.subscription.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import saessak.log.comment.security.principal.PrincipalDetail;
import saessak.log.subscription.dto.SubscriptionResponse;
import saessak.log.subscription.service.SubscriptionService;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @ApiResponse(code = 200, message = "구독하셨습니다.")
    @PostMapping("/posts/{postId}/subscribe")
    public ResponseEntity<SubscriptionResponse> subscribe(@PathVariable(value = "postId") Long postId) {

        String fromUserProfileId = getPrincipal().getProfileId();
        SubscriptionResponse subscriptionResponse = subscriptionService.subscribeUser(fromUserProfileId, postId);

        return ResponseEntity.ok().body(subscriptionResponse);
    }

    @ApiResponse(code = 200, message = "구독 취소")
    @DeleteMapping("/posts/{postId}/unSubscribe")
    public ResponseEntity unSubscribe(@PathVariable(value = "postId") Long postId) {

        String fromUserProfileId = getPrincipal().getProfileId();
        subscriptionService.unSubscribeUser(fromUserProfileId, postId);

        return (ResponseEntity) ResponseEntity.ok();
    }

    private PrincipalDetail getPrincipal() {
        return (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
