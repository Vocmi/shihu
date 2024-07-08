package com.shihu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import shihu.constant.MessageConstant;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-05-30
 */
@Data
@NoArgsConstructor
public class UserLoginDto implements Serializable {
    @NotBlank(message = MessageConstant.USER_NOT_NULL)
    private String username;

    @NotBlank(message =  MessageConstant.PWD_NOT_NULL)
    private String password;

    @Serial
    private static final long serialVersionUID = 1L;
}