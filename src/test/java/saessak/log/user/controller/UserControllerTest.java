package saessak.log.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.repository.SubscriptionRepository;
import saessak.log.user.User;
import saessak.log.user.dto.*;
import saessak.log.user.repository.UserRepository;
import saessak.log.user.service.UserService;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @Autowired TestRestTemplate restTemplate;

    @Autowired UserRepository userRepository;

    @Autowired UserService userService;

    @Autowired SubscriptionRepository subscriptionRepository;

    @Autowired BCryptPasswordEncoder encoder;

    @LocalServerPort int port;

    @BeforeEach
    public void create_user() {
        UserJoinDto userJoinDto1 = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");
        User user1 = userJoinDto1.toEntity();
        User userOne = user1.passwordEncode(encoder);
        userRepository.save(userOne);

        UserJoinDto userJoinDto2 = new UserJoinDto("hi", "1234", "1234", "tyu", "tyu@naver.com");
        User user2 = userJoinDto2.toEntity();
        User userTwo = user2.passwordEncode(encoder);
        userRepository.save(userTwo);

        Subscription subscription = Subscription.of(userOne, userTwo);
        subscriptionRepository.save(subscription);

    }

    @Test
    @DisplayName("회원가입 api 성공")
    public void join() throws Exception {
        //given
        UserJoinDto userJoinDto = new UserJoinDto("ghqls", "1234", "1234", "hobin", "ghqls@naver.com");

        //when
        mockMvc.perform(post("/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userJoinDto)))
            .andExpect(status().isOk());

        //then
        User user = userRepository.findByProfileId(userJoinDto.getProfileId());
        assertThat(user.getEmail()).isEqualTo(userJoinDto.getEmail());
        assertThat(user.getName()).isEqualTo(userJoinDto.getName());
    }

    @Test
    @DisplayName("회원 가입 api 이메일 중복")
    public void join_fail() throws Exception {
        UserJoinDto userJoinDto2 = new UserJoinDto("profileId", "1234", "1234", "name", "hobin@naver.com");

        mockMvc.perform(post("/user/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userJoinDto2)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("아이디 중복 검사 api 성공")
    public void id_duplicate_OK() throws Exception {
        ProfileIdDuplicateDto profileIdDuplicateDto = new ProfileIdDuplicateDto("asd");

        mockMvc.perform(post("/user/duplicate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(profileIdDuplicateDto)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("아이디 중복 검사 api 실패")
    public void id_duplicate_fail() throws Exception {
        ProfileIdDuplicateDto profileIdDuplicateDto = new ProfileIdDuplicateDto("ghdb132");

        mockMvc.perform(post("/user/duplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileIdDuplicateDto)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 api 성공")
    public void login() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("ghdb132", "1234");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    @DisplayName("로그인 api 실패")
    public void login_fail() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("ghdb132", "123456");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("아이디 찾기 api 성공")
    public void find_Id() throws Exception {
        UserFindIdRequest userFindIdRequest = new UserFindIdRequest("hobin", "hobin@naver.com");

        mockMvc.perform(post("/user/findId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userFindIdRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.profileId").value("ghdb132"));

    }

    @Test
    @DisplayName("아이디 찾기 api 실패")
    public void find_Id_fail() throws Exception {
        UserFindIdRequest userFindIdRequest = new UserFindIdRequest("asdf", "hobin@naver.com");

        mockMvc.perform(post("/user/findId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userFindIdRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("비밀번호 찾기 api")
    public void find_Password() throws Exception {
        UserFindPasswordRequest userFindPasswordRequest = new UserFindPasswordRequest("hobin", "ghdb132", "hobin@naver.com");

        mockMvc.perform(post("/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userFindPasswordRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resetPassword").isNotEmpty());
    }

//    @Test
//    @DisplayName("마이페이지")
//    public void myPage() throws Exception {
//        //given
//        String joinUrl = "http://localhost:" + port + "/user/join";
//
//        UserJoinDto userJoinDto1 = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");
//        restTemplate.postForEntity(joinUrl, userJoinDto1, Long.class);
//
//        String loginUrl = "http://localhost:" + port + "/user/login";
//
//        UserLoginDto userLoginDto = new UserLoginDto("ghdb132", "1234");
//        ResponseEntity<Object> loginResponse = restTemplate.postForEntity(loginUrl, userLoginDto, Object.class);
//        Object body = loginResponse.getBody();
//        String token = body.toString().split("=")[1];
//
//        String myPageUrl = "http://localhost:" + port + "/user/information";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
//
//        //when
//        ResponseEntity<UserInformationResponse> responseEntity = restTemplate.exchange(myPageUrl, HttpMethod.GET, httpEntity, UserInformationResponse.class);
//        System.out.println("responseEntity = " + responseEntity);
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody().getProfileId()).isEqualTo("");
//    }
//    @Test
//    public void testUserInformation() throws Exception {
//        // Given
//        String profileId = "ghdb132";
//        UserInformationResponse expectedResponse
//            = new UserInformationResponse(1L, "ghdb132", "hobin@naver.com", "hobin", Arrays.asList(1L, 2L), Arrays.asList(3L, 4L));
//
//        given(userService.userInformation(profileId)).willReturn(expectedResponse);
//
//        // When
//        mockMvc.perform(get("/user/information")
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(user("ghdb132").password("1234").roles("USER"))
//                .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.userId").value(expectedResponse.getUserId()))
//            .andExpect(jsonPath("$.profileId").value(expectedResponse.getProfileId()))
//            .andExpect(jsonPath("$.email").value(expectedResponse.getEmail()))
//            .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
//            .andExpect(jsonPath("$.reactionPostId").value(expectedResponse.getReactionPostId()))
//            .andExpect(jsonPath("$.subscriptionToUserId").value(expectedResponse.getSubscriptionToUserId()));
//
//        // Then
////        verify(userService, times(1)).userInformation(profileId);
////        verifyNoMoreInteractions(userService);
//    }
}
