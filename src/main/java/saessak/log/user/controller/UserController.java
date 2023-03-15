package saessak.log.user.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import saessak.log.jwt.dto.TokenDto;
import saessak.log.user.dto.*;
import saessak.log.user.service.UserService;

@RestController //  RestController 데이터 전송
@RequiredArgsConstructor // 생성자
@RequestMapping(value = "/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Long> createUser(@Validated @RequestBody UserJoinDto userJoinDto) {
        return ResponseEntity.ok().body(userService.join(userJoinDto));
    }

    @ApiOperation(value = "아이디 중복검사")
    @PostMapping("/duplicate")
    public ResponseEntity duplicateProfileId(@RequestBody UserDuplicateDto userDuplicateDto) {
        try {
            userService.duplicateUser(userDuplicateDto);
            return ResponseEntity.ok().body("가입 가능한 아이디입니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 가입하신 회원입니다.");
        }

    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        TokenDto token = userService.login(userLoginDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @ApiOperation(value = "아이디 찾기")
    @PostMapping("/findId")
    public ResponseEntity<ResponseFindIdDto> findProfileId(@RequestBody UserFindIdDto userFindIdDto) {
        ResponseFindIdDto profileId = userService.findProfileId(userFindIdDto);
        return ResponseEntity.status(HttpStatus.OK).body(profileId);
    }

    @ApiOperation(value = "비밀번호 찾기")
    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseResetPasswordDto> findPassword(
            @RequestBody UserFindPasswordDto userFindPasswordDto) {
        ResponseResetPasswordDto resetPassword = userService.findPassword(userFindPasswordDto);
        return ResponseEntity.status(HttpStatus.OK).body(resetPassword);
    }

    @ApiOperation(value = "비밀번호 변경")
    @PatchMapping("/updatePassword")
    public ResponseEntity updatePassword(
            @RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        String profileId = authentication.getName();
        userService.updatePassword(profileId, changePasswordDto);
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경이 완료되었습니다.");
    }

    @ApiOperation(value = "마이페이지")
    @GetMapping("/information")
    public ResponseEntity<ResponseUserInformationDto> userInformation(Authentication authentication){
        String profileId = authentication.getName();
        ResponseUserInformationDto userInformationDto = userService.userInformation(profileId);
        return ResponseEntity.ok().body(userInformationDto);
    }
}
