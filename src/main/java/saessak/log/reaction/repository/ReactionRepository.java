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

    @Query("select r from Reaction r where r.user.id = :userIdx and r.post.id = :postIdx")
    Reaction findByUserIdxAndPostIdx(@Param("userIdx") Long userIdx, @Param("postIdx") Long postIdx);

    @Query("select r from Reaction r" +
        " join fetch r.post p" +
        " join fetch p.postMedia pm" +
        " where r.user.id = :userIdx")
    List<Reaction> findByUserIdx(@Param("userIdx") Long userIdx);
}
