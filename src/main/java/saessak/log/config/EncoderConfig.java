package saessak.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncoderConfig {

    // httpConfig와 BCryptConig는 꼭 다른 클래스에 선언한다. (순환참조 이슈 방지)
    @Bean
    public BCryptPasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }
}
