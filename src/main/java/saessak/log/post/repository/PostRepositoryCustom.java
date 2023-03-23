package saessak.log.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saessak.log.post.dto.PostMainDto;
import saessak.log.post.dto.PostMyActivityDto;
import saessak.log.post.dto.PostResponseDto;
import saessak.log.post.dto.SubscribePostDto;

public interface PostRepositoryCustom {

    Page<PostMainDto> findAllPostMainDtoOrderByLikeCount(Pageable pageable);

    PostResponseDto findPostDetailById(Long postId);

    PostResponseDto findPostDetailById(Long postId, Long userId);

    Page<PostMyActivityDto> findMyPost(Long userId, Pageable pageable);

    Page<SubscribePostDto> findSubscribedPosts(Long userId, Pageable pageable);

}
