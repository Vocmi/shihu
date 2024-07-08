package com.shihu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDetailsVo {

     private String username;
     private Integer likeCount;
     private String title;
     private String content;
     private LocalDateTime updateTime;
     private Integer isLiked;
}
