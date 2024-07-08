package com.shihu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCommentVo {

    private Integer id;
    private String username;
    private String content;
}
