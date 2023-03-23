package saessak.log.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saessak.log.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User findByProfileId(String profileId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndName(String email, String name);

}
