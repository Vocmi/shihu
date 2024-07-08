package com.shihu.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PostSubmitTextDto implements Serializable {
    private String title;
    private String content;

    @Serial
    private static final long serialVersionUID = 1L;
}
