package com.shihu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostPageVo {
    private Integer id;
    private String title;
    private String content;
    private Integer likeCount;
    private Integer viewCount;
    private Integer replyCount;

}
