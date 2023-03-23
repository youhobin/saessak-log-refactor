package saessak.log.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saessak.log.user.User;

import java.util.Optional;

public interface UserCustomRepository {

    Optional<User> findByUserInfo(String email, String name, String profileId);

    Optional<User> findOptionalByProfileId(String profileId);

    User findWithReactionsByProfileId(String profileId);

}
