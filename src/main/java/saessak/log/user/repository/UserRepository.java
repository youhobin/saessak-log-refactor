package saessak.log.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saessak.log.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    User findByProfileId(String profileId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndName(String email, String name);

}
