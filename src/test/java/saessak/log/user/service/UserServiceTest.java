package saessak.log.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.jwt.dto.TokenResponse;
import saessak.log.user.User;
import saessak.log.user.dto.ProfileIdDuplicateDto;
import saessak.log.user.dto.UserJoinDto;
import saessak.log.user.dto.UserLoginDto;
import saessak.log.user.error.DuplicateEmailException;
import saessak.log.user.error.DuplicateLoginIdException;
import saessak.log.user.error.NotMatchPasswordException;
import saessak.log.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeEach
    public void user() {
        UserJoinDto userJoinDto = new UserJoinDto("hobin", "1234", "1234", "name", "hobin@naver.com");
        userService.join(userJoinDto);
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복 확인")
    public void duplicate_email() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");

        //then
        assertThatThrownBy(() -> userService.join(userJoinDto))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("회원가입 시 비밀번호 일치 확인")
    public void not_match_password() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("ghdb132", "1234", "12345", "hobin", "hobin11@naver.com");

        //then
        assertThatThrownBy(() -> userService.join(userJoinDto))
            .isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    @DisplayName("회원가입 시 아이디 중복 검사")
    public void duplicate_profileId() throws Exception {
        //given
        ProfileIdDuplicateDto profileIdDuplicateDto = new ProfileIdDuplicateDto("hobin");

        //then
        assertThatThrownBy(() -> userService.duplicateUser(profileIdDuplicateDto))
            .isInstanceOf(DuplicateLoginIdException.class);
    }

    @Test
    @DisplayName("로그인 시 등록되지 않은 회원 아이디")
    public void login_wrong_id() throws Exception {
        //given
        UserLoginDto loginDto = new UserLoginDto("hobin2", "1234");

        //then
        assertThatThrownBy(() -> userService.login(loginDto))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("등록되지 않은 회원입니다.");
    }

    @Test
    @DisplayName("로그인 시 틀린 비밀번호")
    public void login_wrong_password() throws Exception {
        //given
        UserLoginDto loginDto = new UserLoginDto("hobin", "12345");

        //then
        assertThatThrownBy(() -> userService.login(loginDto))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("비밀번호가 잘못되었습니다.");
    }

    @Test
    public void login() throws Exception {
        //given
        UserLoginDto loginDto = new UserLoginDto("hobin", "1234");

        //when
        TokenResponse token = userService.login(loginDto);

        //then
        assertThat(token.getToken()).isNotNull();
    }
}
