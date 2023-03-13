package saessak.log.subscription.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Contact;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import saessak.log.user.User;

import javax.persistence.EntityListeners;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionDto {

    @ApiModelProperty(value = "구독자 아이디", dataType = "int", required = true)
    @Getter
    private Long user;
}

