package saessak.log.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import saessak.log.jwt.CustomAuthenticationEntryPoint;
import saessak.log.jwt.JwtFilter;
import saessak.log.user.service.UserService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // ui에서 들어오는 것. auth 기반의 로그인창이 안뜨도록 설정.(security 사용하면 기본 로그인 창이있음)
                    .csrf().disable() // crosssite 기능. csrf 보안 기능이 rest api 에서 안쓰이므로 disable.
                    .cors()
                .and()// crosssite 다른 domain 허용
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                    .authorizeRequests()
                        .antMatchers("/user").permitAll() // user 권한 허용 // 이거 다시
                        .antMatchers("/user/join", "/user/login", "/user/duplicate", "/user/findId", "/user/resetPassword").permitAll() // join, login 허용
                        .antMatchers(HttpMethod.GET, "/posts/{postId}/comments").permitAll()
                        .antMatchers(HttpMethod.GET,"/posts/likeCount").permitAll()
                        .antMatchers(HttpMethod.GET,"/posts/commentsCount").permitAll()
                        .antMatchers(HttpMethod.GET,"/posts/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/comments/**", "/posts/**").authenticated() // 아니면 /*까지 해보기.
                        .antMatchers("/subscribe/**").authenticated()
                        .antMatchers("/like/**").authenticated()
//                .antMatchers(HttpMethod.GET, "/coments", "/posts").authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용할 경우 세션을 사용하지 않는다.
                .and()
                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class) // 토큰인가 전 로그인 검증
                .build();
    }
}
