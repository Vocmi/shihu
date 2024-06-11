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
public class UserRegisterDto implements Serializable {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String verifyCode;

    @Serial
    private static final long serialVersionUID = 1L;
}