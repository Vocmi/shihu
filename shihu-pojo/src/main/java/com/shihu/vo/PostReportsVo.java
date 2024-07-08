package com.shihu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostReportsVo {
    private Integer reporterId;
    private Integer postId;
    private String reason;
}
