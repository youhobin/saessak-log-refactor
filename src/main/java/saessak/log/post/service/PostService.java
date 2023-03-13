package saessak.log.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import saessak.log.post.Post;
import saessak.log.post.dto.*;
import saessak.log.post.repository.PostRepository;
import saessak.log.post_media.PostMedia;
import saessak.log.post_media.repository.PostMediaRepository;
import saessak.log.user.User;
import saessak.log.user.repository.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMediaRepository postMediaRepository;

    @Transactional
    public Long savePost(String profileId, String postText, MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();
        String encodedImageFile = Base64.getEncoder().encodeToString(bytes);
        String imageFile = "data:" + contentType + ";base64," + encodedImageFile;

        String pythonApiUrl = "http://3.39.201.8:5000/file_upload";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestJson = new JSONObject();
        requestJson.put("imageFile", imageFile);
        requestJson.put("fileName", originalFilename);

        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(requestJson).toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, request, String.class);

        User user = userRepository.findByProfileId(profileId);
        Post post = Post.from(user);
        Post savedPost = postRepository.save(post);

        //1. 이미지 태그 기능 사용 시 : 먼저 저장방법
        //response에 imageFileName 이랑 이미지에서 추출한 태그 List(?)가 아마 들어있을듯.
        //imageFileName 은 기존과 똑같이 값을 가져와서 (getBody에서 바뀔듯. body의 값이 2개가 되면) PostMedia 에 저장.
        // tag 값은 일단 1번 : List로 들어오면 이거를 String 으로 (예시 : "a,b,c"처럼 바꿔서 PostMedia에 컬럼을 imageTag 같이 하나 만들어서) 저장.
        //  -> 밑에처럼 3개의 값으로 postMediaRepository 에 저장.
        // 2. 저장이후 게시글 조회 시 : 현재 보여주는 값에 + 태그값? imageTag 는 보내줄지 프론트에 물어보기.
        // 3. 검색 기능 추가 시 : 아마 태그로 검색?
        // 태그로 postMedia에서 검색해서 맞는 값을 가져옴. 쿼리의 like 절? 보여주는 거는 전체 조회처럼 보내주면 될듯.
        String imageFileName = response.getBody();
        log.info("imageFileName={}", imageFileName);

        PostMedia postMedia = PostMedia.of(imageFileName, postText);
        postMedia.belongToPost(post);
        postMediaRepository.save(postMedia);

        return savedPost.getId();
    }

    public PostResponseDto findPost(Long postId, String userProfileId) {
        User user = userRepository.findByProfileId(userProfileId);
        PostResponseDto postResponseDto;
        if (user == null)
            postResponseDto = postRepository.findPostDetailById(postId);
        else
            postResponseDto = postRepository.findPostDetailById(postId, user.getId());
        return postResponseDto;
    }

    public PostAllResponseDto findAllPostsByLikeCount(String profileId, int limit, int page) {
        User user = userRepository.findByProfileId(profileId);
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDto;
        if (user == null)
            pagePostMainDto = postRepository.findAllPostMainDtoOrderByLikeCount(pageRequest);
        else
            pagePostMainDto = postRepository.findAllPostMainDtoOrderByLikeCount(pageRequest, user.getId());
        List<PostMainDto> postMainDtoList = pagePostMainDto.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }

    public PostAllResponseDto findAllPostsByCommentsCount(String profileId, int limit, int page) {
        User user = userRepository.findByProfileId(profileId);
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDtoList;
        if (user == null)
            pagePostMainDtoList = postRepository.findAllPostMainDtoOrderByCommentCount(pageRequest);
        else
            pagePostMainDtoList = postRepository.findAllPostMainDtoOrderByCommentCount(pageRequest, user.getId());
        List<PostMainDto> postMainDtoList = pagePostMainDtoList.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }

    // 편의 기능 함수 추가 --아연
    public Long findUserIndexByPostId(Long postIndex) {
        Post foundedPost = postRepository.findById(postIndex).orElseThrow(() -> new IllegalArgumentException());
        return foundedPost.getUser().getId();
    }

    public MyActivitiesResponse getMyActivity(String profileId, Integer page, Integer limit) {
        User findUser = userRepository.findByProfileId(profileId);
        Long userId = findUser.getId();

        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<PostMyActivityDto> pageMyPost = postRepository.findMyPost(userId, pageRequest);
        List<PostMyActivityDto> myActivityPosts = pageMyPost.getContent();
        return new MyActivitiesResponse(myActivityPosts);
    }

    public SubscribePostResponse getSubscribedPosts(String profileId, int page, int limit) {
        User findUser = userRepository.findByProfileId(profileId);
        Long userId = findUser.getId();

        PageRequest pageRequest = PageRequest.of(page, limit);
        List<SubscribePostDto> subscribedPostList = postRepository.findSubscribedPosts(userId, pageRequest);
        return new SubscribePostResponse(subscribedPostList);
    }
}
