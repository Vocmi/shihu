package com.shihu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
public class PostLikeDto {
    private Integer user_id;
    private Integer post_id;
    private Integer is_liked;

    @Serial
    private static final long serialVersionUID = 1L;
}
