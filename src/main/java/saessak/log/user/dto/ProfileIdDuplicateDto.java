package saessak.log.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileIdDuplicateDto {

    @NotNull
    private String profileId;

}
