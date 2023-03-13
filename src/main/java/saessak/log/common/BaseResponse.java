package saessak.log.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class BaseResponse {
    @ApiModelProperty(name = "기본 응답 형식")
    public String responseMessage;
}
