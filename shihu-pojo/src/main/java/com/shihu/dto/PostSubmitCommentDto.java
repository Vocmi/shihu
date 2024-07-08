package com.shihu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSubmitCommentDto {
    private Integer userId;
    private Integer postId;
    private String content;
}
