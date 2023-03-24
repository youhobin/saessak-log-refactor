package saessak.log.post.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import saessak.log.post.Post;
import saessak.log.post.QPost;
import saessak.log.post.dto.PostMainDto;
import saessak.log.post.dto.PostMyActivityDto;
import saessak.log.post.dto.PostResponseDto;
import saessak.log.post.dto.SubscribePostDto;
import saessak.log.reaction.QReaction;
import saessak.log.subscription.QSubscription;

import javax.persistence.EntityManager;

import java.util.List;

import static saessak.log.post.QPost.post;
import static saessak.log.post_media.QPostMedia.postMedia;
import static saessak.log.reaction.QReaction.reaction;
import static saessak.log.subscription.QSubscription.subscription;
import static saessak.log.user.QUser.user;

public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostMainDto> findAllPostMainDtoOrderByLikeCount(Pageable pageable) {
        List<PostMainDto> content = queryFactory
            .select(Projections.constructor(PostMainDto.class,
                post.id,
                user.profileId,
                postMedia.imageFile,
                post.commentsCount,
                post.reactionCount
            ))
            .from(post)
            .leftJoin(post.postMedia, postMedia)
            .leftJoin(post.user, user)
            .groupBy(post.id)
            .orderBy(post.reactionCount.desc(), post.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(post.count())
            .from(post);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public PostResponseDto findPostDetailById(Long postId) {
        return queryFactory
            .select(Projections.constructor(PostResponseDto.class,
                user.profileId,
                postMedia.imageFile,
                postMedia.postText,
                post.reactionCount
            ))
            .from(post)
            .leftJoin(post.postMedia, postMedia)
            .leftJoin(post.user, user)
            .where(post.id.eq(postId))
            .fetchOne();
    }

    @Override
    public PostResponseDto findPostDetailById(Long postId, String profileId) {
        return queryFactory
            .select(Projections.constructor(PostResponseDto.class,
                user.profileId,
                postMedia.imageFile,
                postMedia.postText,
                post.reactionCount,
                ExpressionUtils.as(
                    JPAExpressions.selectOne()
                        .from(subscription)
                        .where(subscription.fromUserId.profileId.eq(profileId)
                            .and(subscription.toUserId.id.eq(user.id)))
                        .exists(),
                    "subscribed"
                ),
                ExpressionUtils.as(
                    JPAExpressions.selectOne()
                        .from(reaction)
                        .where(reaction.post.id.eq(post.id)
                            .and(reaction.user.profileId.eq(profileId)))
                        .exists(),
                    "liked"
                )
            ))
            .from(post)
            .leftJoin(post.postMedia, postMedia)
            .leftJoin(post.user, user)
            .where(post.id.eq(postId))
            .groupBy(user.profileId, postMedia.imageFile, postMedia.postText, post.reactionCount)
            .fetchOne();
    }

    @Override
    public Page<PostMyActivityDto> findMyPost(String profileId, Pageable pageable) {
        List<PostMyActivityDto> content = queryFactory
            .select(Projections.constructor(PostMyActivityDto.class,
                post.id,
                postMedia.imageFile,
                post.commentsCount,
                post.reactionCount)
            )
            .from(post)
            .leftJoin(post.user, user)
            .leftJoin(post.postMedia, postMedia)
            .where(user.profileId.eq(profileId))
            .groupBy(post.id)
            .orderBy(post.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(post.count())
            .from(post);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<SubscribePostDto> findSubscribedPosts(Long userId, Pageable pageable) {
        List<SubscribePostDto> content = queryFactory
            .select(Projections.constructor(SubscribePostDto.class,
                post.id,
                user.profileId,
                postMedia.imageFile,
                post.commentsCount,
                post.reactionCount,
                ExpressionUtils.as(
                    JPAExpressions.selectOne()
                        .from(reaction)
                        .where(reaction.user.id.eq(userId)
                            .and(reaction.post.id.eq(post.id)))
                        .exists(),
                    "liked"
                )
            ))
            .from(post)
            .leftJoin(post.user, user)
            .leftJoin(post.postMedia, postMedia)
            .leftJoin(subscription)
            .on(subscription.toUserId.id.eq(user.id), subscription.fromUserId.id.eq(userId))
            .leftJoin(reaction)
            .on(reaction.user.id.eq(userId), reaction.post.id.eq(post.id))
            .where(subscription.toUserId.id.eq(user.id), subscription.fromUserId.id.eq(userId))
            .groupBy(post.id)
            .orderBy(post.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(post.count())
            .from(post);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
