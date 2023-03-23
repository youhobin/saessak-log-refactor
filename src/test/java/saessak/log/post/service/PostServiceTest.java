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
import saessak.log.post.dto.*;
import saessak.log.post.repository.PostRepository;
import saessak.log.post_media.PostMedia;
import saessak.log.reaction.Reaction;
import saessak.log.reaction.repository.ReactionRepository;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.repository.SubscriptionRepository;
import saessak.log.user.User;
import saessak.log.user.dto.UserJoinDto;
import saessak.log.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired PostRepository postRepository;
    @Autowired PostService postService;
    @Autowired UserRepository userRepository;
    @Autowired BCryptPasswordEncoder encoder;
    @Autowired ReactionRepository reactionRepository;
    @Autowired SubscriptionRepository subscriptionRepository;

    static User user;
    static Post post;

    @BeforeEach
    void init() {
        UserJoinDto userJoinDto1 = new UserJoinDto("ghdb132", "1234", "1234", "hobin", "hobin@naver.com");
        User user1 = userJoinDto1.toEntity();
        user = user1.passwordEncode(encoder);
        userRepository.save(user);

        PostMedia postMedia = PostMedia.of("image1", "text1");
        post = Post.of(user, postMedia);
        postRepository.save(post);

        PostMedia postMedia2 = PostMedia.of("image2", "text2");
        Post post2 = Post.of(user, postMedia2);
        postRepository.save(post2);

        PostMedia postMedia3 = PostMedia.of("image3", "text3");
        Post post3 = Post.of(user, postMedia3);
        postRepository.save(post3);

        Reaction reaction = Reaction.of(user, post);
        reactionRepository.save(reaction);
        post.plusReactionCount();
    }

    @Test
    @DisplayName("게시글 단건 조회 - 로그인 X")
    public void findPost() throws Exception {
        //when
        PostResponseDto postResponseDto = postService.findPost(post.getId());

        //then
        assertThat(postResponseDto.getPostText()).isEqualTo(post.getPostMedia().getPostText());
        assertThat(postResponseDto.getProfileId()).isEqualTo(user.getProfileId());
        assertThat(postResponseDto.getLikeCount()).isEqualTo(1);
        assertThat(postResponseDto.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + post.getPostMedia().getImageFile());
        assertThat(postResponseDto.isLike()).isFalse();
        assertThat(postResponseDto.isSubscribe()).isFalse();
    }

    @Test
    @DisplayName("게시글 단건 조회 - 로그인 O")
    public void findPost_Login() throws Exception {
        //when
        PostResponseDto postResponseDto = postService.findPost(post.getId(), user.getProfileId());

        //then
        assertThat(postResponseDto.getPostText()).isEqualTo(post.getPostMedia().getPostText());
        assertThat(postResponseDto.getProfileId()).isEqualTo(user.getProfileId());
        assertThat(postResponseDto.getLikeCount()).isEqualTo(1);
        assertThat(postResponseDto.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/" + post.getPostMedia().getImageFile());
        assertThat(postResponseDto.isLike()).isTrue();
    }

    @Test
    @DisplayName("메인 페이지 - 게시글 전체조회 - 좋아요 갯수 정렬")
    public void findAllPostsByLikeCount() throws Exception {
        //given
        int limit = 2;
        int page = 0;

        //when
        PostAllResponseDto postAllResponseDto = postService.findAllPostsByLikeCount(limit, page);
        List<PostMainDto> postMainDtoList = postAllResponseDto.getPostMainDtoList();
        PostMainDto post1 = postMainDtoList.get(0);
        PostMainDto post2 = postMainDtoList.get(1);

        //then
        assertThat(postMainDtoList.size()).isEqualTo(2);
        assertThat(post1.getProfileId()).isEqualTo("ghdb132");
        assertThat(post1.getReactionCount()).isEqualTo(1);
        assertThat(post1.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image1");

        assertThat(post2.getProfileId()).isEqualTo("ghdb132");
        assertThat(post2.getReactionCount()).isEqualTo(0);
        assertThat(post2.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image3");
    }

    @Test
    @DisplayName("내 활동")
    public void myActivity() throws Exception {
        //given
        int page = 0;
        int limit = 2;

        //when
        MyActivitiesResponse myActivity = postService.getMyActivity("ghdb132", page, limit);
        List<PostMyActivityDto> postMyActivityDtoList = myActivity.getPostMyActivityDtoList();
        PostMyActivityDto recentMyActivity1 = postMyActivityDtoList.get(0);
        PostMyActivityDto recentMyActivity2 = postMyActivityDtoList.get(1);


        //then
        assertThat(recentMyActivity1.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image3");
        assertThat(recentMyActivity1.getReactionCount())
            .isEqualTo(0);

        assertThat(recentMyActivity2.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image2");
        assertThat(recentMyActivity2.getReactionCount())
            .isEqualTo(0);
    }

    @Test
    @DisplayName("구독한 사람의 게시물 가져오기")
    public void subscribePost() throws Exception {
        //given
        UserJoinDto userJoinDto1 = new UserJoinDto("user2", "1234", "1234", "erer", "user2@naver.com");
        User user2 = userJoinDto1.toEntity();
        user2.passwordEncode(encoder);
        userRepository.save(user2);

        Subscription subscription = Subscription.of(user, user2);
        subscriptionRepository.save(subscription);

        //when
        SubscribePostResponse subscribePostResponse = postService.getSubscribedPosts("user2", 0, 3);
        List<SubscribePostDto> subscribePostList = subscribePostResponse.getSubscribePostList();
        SubscribePostDto post3 = subscribePostList.get(0);
        SubscribePostDto post1 = subscribePostList.get(2);

        //then
        assertThat(post3.getProfileId()).isEqualTo("ghdb132");
        assertThat(post3.getReactionCount()).isEqualTo(0);
        assertThat(post3.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image3");

        assertThat(post1.getReactionCount()).isEqualTo(1);
        assertThat(post1.getImageFile())
            .isEqualTo("https://saessaklogfile.s3.ap-northeast-2.amazonaws.com/image/image1");
    }
}
