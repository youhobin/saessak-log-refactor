package saessak.log.post_media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saessak.log.post.dto.PostResponseDto;
import saessak.log.post_media.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {

}
