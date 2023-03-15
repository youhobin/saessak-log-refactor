package saessak.log.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.user.User;
import saessak.log.user.dto.ProfileIdDuplicateDto;
import saessak.log.user.dto.UserJoinDto;
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

    @BeforeEach
    public void user() {
        User user = User.of("hobin", "1234", "name", "hobin@naver.com");
        userRepository.save(user);
    }

    @Test
    @DisplayName("회원가입시 이메일 중복 확인")
    public void duplicate_email() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");

        //then
        assertThatThrownBy(() -> userService.join(userJoinDto))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("회원가입시 비밀번호 일치 확인")
    public void not_match_password() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("ghdb132", "1234", "12345", "hobin", "hobin11@naver.com");

        //then
        assertThatThrownBy(() -> userService.join(userJoinDto))
            .isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    public void duplicate_profileId() throws Exception {
        //given
        ProfileIdDuplicateDto profileIdDuplicateDto = new ProfileIdDuplicateDto("hobin");

        //then
        assertThatThrownBy(() -> userService.duplicateUser(profileIdDuplicateDto))
            .isInstanceOf(DuplicateLoginIdException.class);
    }


}
