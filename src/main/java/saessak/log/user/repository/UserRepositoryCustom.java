package saessak.log.user.repository;

import saessak.log.user.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByUserInfo(String email, String name, String profileId);

    Optional<User> findOptionalByProfileId(String profileId);

    User findWithReactionsByProfileId(String profileId);

}
