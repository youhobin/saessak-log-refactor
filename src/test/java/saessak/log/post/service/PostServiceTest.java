package saessak.log.post.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.post.Post;
import saessak.log.post.dto.PostResponseDto;
import saessak.log.post.repository.PostRepository;
import saessak.log.post_media.PostMedia;
import saessak.log.user.User;
import saessak.log.user.dto.UserJoinDto;
import saessak.log.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired PostRepository postRepository;
    @Autowired PostService postService;
    @Autowired UserRepository userRepository;
    @Autowired BCryptPasswordEncoder encoder;

    static User user;
    static Post post;

    @BeforeEach
    void init() {
        UserJoinDto userJoinDto1 = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");
        User user1 = userJoinDto1.toEntity();
        user = user1.passwordEncode(encoder);
        userRepository.save(user);

        PostMedia postMedia = PostMedia.of("imageFile", "text");
        post = Post.of(user, postMedia);
        postRepository.save(post);
    }

    @Test
    @DisplayName("게시글 단건 조회 - 로그인 X")
    public void findPost() throws Exception {
        //when
        PostResponseDto postResponseDto = postService.findPost(post.getId());

        //then
        assertThat(postResponseDto.getPostText()).isEqualTo(post.getPostMedia().getPostText());
        assertThat(postResponseDto.getProfileId()).isEqualTo(user.getProfileId());
        assertThat(postResponseDto.getLikeCount()).isEqualTo(0);
        assertThat(postResponseDto.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + post.getPostMedia().getImageFile());
    }


}
