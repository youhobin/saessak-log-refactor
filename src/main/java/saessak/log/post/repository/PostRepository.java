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

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "select new saessak.log.post.dto.PostMainDto(p.id, u.profileId, pm.imageFile, p.commentsCount, p.reactionCount) " +
            "from Post p " +
            "left join p.postMedia pm " +
            "left join p.user u " +
            "group by p.id " +
            "order by p.reactionCount desc, p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<PostMainDto> findAllPostMainDtoOrderByLikeCount(Pageable pageable);




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

    @Query("select new saessak.log.post.dto.PostResponseDto(u.profileId, pm.imageFile, pm.postText, p.reactionCount)" +
            " from Post p" +
            " left join p.postMedia pm" +
            " left join p.user u" +
            " where p.id = :postId")
    PostResponseDto findPostDetailById(@Param("postId") Long postId);

    @Query(value = "select new saessak.log.post.dto.PostResponseDto(u.profileId, pm.imageFile, pm.postText, p.reactionCount, count(s) > 0, count(r) > 0)" +
            " from Post p" +
            " left join p.postMedia pm" +
            " left join p.user u" +
            " left join Subscription s" +
            " on s.fromUserId.id = :userId" +
            " and s.toUserId.id = u.id" +
            " left join Reaction r" +
            " on r.post.id = p.id" +
            " and r.user.id = :userId" +
            " where p.id = :postId",
        countQuery = "select count(p) from Post p")
    PostResponseDto findPostDetailById(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query(value = "select new saessak.log.post.dto.PostMyActivityDto(p.id, pm.imageFile, p.commentsCount, p.reactionCount)" +
            " from Post p" +
            " left join p.user u" +
            " left join p.postMedia pm" +
            " where u.id = :userId" +
            " group by p.id" +
            " order by p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<PostMyActivityDto> findMyPost(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select new saessak.log.post.dto.SubscribePostDto(p.id, u.profileId, pm.imageFile, p.commentsCount, p.reactionCount, count(r) > 0)" +
            " from Post p, Subscription s" +
            " left join p.user u" +
            " left join p.postMedia pm" +
            " left join s.toUserId tu" +
            " left join s.fromUserId fu" +
            " left join Reaction r" +
            " on r.user.id = :userId" +
            " and r.post.id = p.id" +
            " where tu.id = u.id" +
            " and fu.id = :userId" +
            " group by p.id" +
            " order by p.createdDate desc",
        countQuery = "select count(p) from Post p")
    Page<SubscribePostDto> findSubscribedPosts(@Param("userId") Long userId, Pageable pageable);

    @Query("select p from Post p" +
        " join fetch p.user u" +
        " where p.id = :postId")
    Post findWithUserById(@Param("postId") Long postId);

}
