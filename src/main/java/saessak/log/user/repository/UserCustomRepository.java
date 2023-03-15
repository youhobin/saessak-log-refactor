package saessak.log.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saessak.log.user.User;

import java.util.Optional;

public interface UserCustomRepository {

    @Query("select u from User u where u.profileId = :profileId and u.name = :name and u.email = :email")
    Optional<User> findByUserInfo(@Param("email") String email, @Param("name") String name, @Param("profileId") String profileId);

    @Query("select u from User u where u.profileId = :profileId")
    Optional<User> findOptionalByProfileId(@Param("profileId") String profileId);

    @Query("select u from User u" +
        " join fetch u.reactions r" +
        " where u.profileId = :profileId")
    User findWithReactionsByProfileId(@Param("profileId") String profileId);

}
