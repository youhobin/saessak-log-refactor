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
import saessak.log.post.exception.ImageSaveException;
import saessak.log.post.repository.PostRepository;
import saessak.log.post_media.PostMedia;
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

    @Transactional
    public Long savePost(String profileId, String postText, MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        String imageFile = parseFileToString(file, contentType);

        String pythonApiUrl = "http://3.39.201.8:5000/file_upload";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestJson = new JSONObject();
        requestJson.put("imageFile", imageFile);
        requestJson.put("fileName", originalFilename);

        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request;
        try {
            request = new HttpEntity<>(mapper.writeValueAsString(requestJson).toString(), headers);
        } catch (JsonProcessingException e) {
            throw new ImageSaveException("이미지를 저장할 수 없습니다.", e);
        }

        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, request, String.class);


        String imageFileName = response.getBody();
        log.info("imageFileName={}", imageFileName);

        User user = userRepository.findByProfileId(profileId);
        PostMedia postMedia = PostMedia.of(imageFileName, postText);
        Post post = Post.of(user, postMedia);

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    public PostResponseDto findPost(Long postId, String profileId) {
        PostResponseDto postResponseDto = postRepository.findPostDetailById(postId, profileId);
        return postResponseDto;
    }

    public PostResponseDto findPost(Long postId) {
        PostResponseDto postResponseDto = postRepository.findPostDetailById(postId);
        return postResponseDto;
    }

    public PostAllResponseDto findAllPostsByLikeCount(String profileId, int limit, int page) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDto = postRepository.findAllPostMainDtoOrderByLikeCount(pageRequest, profileId);
        List<PostMainDto> postMainDtoList = pagePostMainDto.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }

    public PostAllResponseDto findAllPostsByLikeCount(int limit, int page) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDto = postRepository.findAllPostMainDtoOrderByLikeCount(pageRequest);
        List<PostMainDto> postMainDtoList = pagePostMainDto.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }

    public PostAllResponseDto findAllPostsByCommentsCount(String profileId, int limit, int page) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDtoList = postRepository.findAllPostMainDtoOrderByCommentCount(pageRequest, profileId);
        List<PostMainDto> postMainDtoList = pagePostMainDtoList.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }

    public PostAllResponseDto findAllPostsByCommentsCount(int limit, int page) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<PostMainDto> pagePostMainDtoList = postRepository.findAllPostMainDtoOrderByCommentCount(pageRequest);
        List<PostMainDto> postMainDtoList = pagePostMainDtoList.getContent();
        return new PostAllResponseDto(postMainDtoList);
    }


    public MyActivitiesResponse getMyActivity(String profileId, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<PostMyActivityDto> pageMyPost = postRepository.findMyPost(profileId, pageRequest);
        List<PostMyActivityDto> myActivityPosts = pageMyPost.getContent();
        return new MyActivitiesResponse(myActivityPosts);
    }

    public SubscribePostResponse getSubscribedPosts(String profileId, int page, int limit) {
        User findUser = userRepository.findByProfileId(profileId);
        Long userId = findUser.getId();

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<SubscribePostDto> subscribedPosts = postRepository.findSubscribedPosts(userId, pageRequest);
        List<SubscribePostDto> subscribedPostList = subscribedPosts.getContent();
        return new SubscribePostResponse(subscribedPostList);
    }

    private static String parseFileToString(MultipartFile file, String contentType) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new ImageSaveException("이미지를 저장할 수 없습니다.", e);
        }
        String encodedImageFile = Base64.getEncoder().encodeToString(bytes);
        String imageFile = "data:" + contentType + ";base64," + encodedImageFile;
        return imageFile;
    }
}
