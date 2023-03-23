package saessak.log.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import saessak.log.reaction.QReaction;
import saessak.log.user.QUser;
import saessak.log.user.User;

import java.util.Optional;

import static saessak.log.reaction.QReaction.reaction;
import static saessak.log.user.QUser.user;


public class UserCustomRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<User> findByUserInfo(String email, String name, String profileId) {
        return Optional.ofNullable(
            queryFactory
                .select(user)
                .from(user)
                .where(user.email.eq(email)
                        .and(user.name.eq(name))
                        .and(user.profileId.eq(profileId)))
                .fetchOne());
    }

    @Override
    public Optional<User> findOptionalByProfileId(String profileId) {
        return Optional.ofNullable(
            queryFactory
                .select(user)
                .from(user)
                .where(user.profileId.eq(profileId))
                .fetchOne());
    }

    @Override
    public User findWithReactionsByProfileId(String profileId) {
        return queryFactory
            .select(user)
            .from(user)
            .leftJoin(user.reactions, reaction).fetchJoin()
            .where(user.profileId.eq(profileId))
            .fetchOne();
    }
}
