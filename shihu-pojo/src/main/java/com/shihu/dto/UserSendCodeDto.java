package com.shihu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-05-30
 */
@Data
@NoArgsConstructor
public class UserSendCodeDto implements Serializable {
    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @Serial
    private static final long serialVersionUID = 1L;
}
