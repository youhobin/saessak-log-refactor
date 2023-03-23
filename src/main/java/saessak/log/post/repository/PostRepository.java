package saessak.log.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.comment.Comment;
import saessak.log.post.Post;
import saessak.log.post.dto.PostMainDto;
import saessak.log.post.dto.PostMyActivityDto;
import saessak.log.post.dto.PostResponseDto;
import saessak.log.post.dto.SubscribePostDto;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {


    @Query(value = "select new saessak.log.post.dto.PostMainDto(p.id, u.profileId, pm.imageFile, p.commentsCount, p.reactionCount, count(r) > 0) " +
            "from Post p " +
            "left join p.postMedia pm " +
            "left join p.user u " +
            "left join Reaction r " +
            "on r.user.id = :userId " +
            "and r.post.id = p.id " +
            "group by p.id " +
            "order by p.reactionCount desc, p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<PostMainDto> findAllPostMainDtoOrderByLikeCount(Pageable pageable, @Param("userId") Long userId);

    @Query(value = "select new saessak.log.post.dto.PostMainDto(p.id, u.profileId, pm.imageFile, p.commentsCount, p.reactionCount) " +
            "from Post p " +
            "left join p.postMedia pm " +
            "left join p.user u " +
            "group by p.id " +
            "order by p.commentsCount desc, p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<PostMainDto> findAllPostMainDtoOrderByCommentCount(Pageable pageable);

    @Query(value = "select new saessak.log.post.dto.PostMainDto(p.id, u.profileId, pm.imageFile, p.commentsCount, p.reactionCount, count(r) > 0) " +
            "from Post p " +
            "left join p.postMedia pm " +
            "left join p.user u " +
            "left join Reaction r " +
            "on r.user.id = :userId " +
            "and r.post.id = p.id " +
            "group by p.id " +
            "order by p.commentsCount desc, p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<PostMainDto> findAllPostMainDtoOrderByCommentCount(Pageable pageable, @Param("userId") Long userId);



    @Query("select p from Post p" +
        " join fetch p.user u" +
        " where p.id = :postId")
    Post findWithUserById(@Param("postId") Long postId);

}
