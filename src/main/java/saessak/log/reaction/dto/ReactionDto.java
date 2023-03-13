package saessak.log.reaction.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ReactionDto {

    private Long userId;

}
