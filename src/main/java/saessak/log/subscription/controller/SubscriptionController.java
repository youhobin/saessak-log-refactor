package saessak.log.subscription.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saessak.log.common.BaseResponse;
import saessak.log.post.service.PostService;
import saessak.log.subscription.dto.SubscriptionDto;
import saessak.log.subscription.service.SubscriptionService;
import saessak.log.user.service.UserService;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final PostService postService;

    @ApiOperation(
            value = "구독",
            notes = "구독하지 않은 사용자에 대해 호출시 해당 사용자를 구독, 이미 구독된 사용자에 대해 호출 시 구독 취소",
            response = BaseResponse.class
    )
    @ApiResponse(code = 200, message = "구독하셨습니다.", response = BaseResponse.class)
    @GetMapping("/subscribe/{post}")
    public Object subscribe(@PathVariable(value = "post") Long post,
                            Authentication authentication) {
        Long toUserId = postService.findUserIndexByPostId(post);
        String fromUserProfileId = authentication.getName();
        Boolean subscribe = subscriptionService.subscribe(fromUserProfileId, toUserId);
        String responseMessage = "";
        if (subscribe) responseMessage = "구독하셨습니다.";
        else responseMessage = "구독을 취소하셨습니다.";
        return ResponseEntity.ok().body(BaseResponse.builder().responseMessage(responseMessage).build());
    }


}
