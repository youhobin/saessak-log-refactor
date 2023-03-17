package saessak.log.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saessak.log.reaction.Reaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("select r from Reaction r" +
        " join fetch r.user u" +
        " where u.profileId = :profileId and r.post.id = :postId")
    Optional<Reaction> findReaction(@Param("postId") Long postId, @Param("profileId") String profileId);
}
