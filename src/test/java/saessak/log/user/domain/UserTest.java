package saessak.log.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import saessak.log.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Bean
    public BCryptPasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("패스워드 인코딩")
    public void passwordEncode() throws Exception {
        //given
        String password = "1234";
        User user = User.of("hobin", password, "유호빈", "yhb@naver.com");

        //when
        user.passwordEncode(encoder());

        //then
        assertThat(password).isNotEqualTo(user.getPassword());
    }

}

