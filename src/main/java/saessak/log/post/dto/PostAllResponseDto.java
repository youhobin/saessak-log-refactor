package saessak.log.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostAllResponseDto {

    List<PostMainDto> postMainDtoList;

    public PostAllResponseDto(List<PostMainDto> postMainDtoList) {
        this.postMainDtoList = postMainDtoList;
    }
}
