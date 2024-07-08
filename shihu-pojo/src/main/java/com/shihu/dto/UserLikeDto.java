package com.shihu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
public class UserLikeDto {
    private Integer userId;
    private Integer postId;
    private Integer isLike;

    @Serial
    private static final long serialVersionUID = 1L;
}
