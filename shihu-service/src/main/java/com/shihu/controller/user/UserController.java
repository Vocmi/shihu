package com.shihu.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
import com.shihu.dto.UserRegisterDto;
import com.shihu.dto.UserSendCodeDto;
import com.shihu.entity.User;
import com.shihu.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shihu.result.Result;

import java.time.LocalDateTime;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
@RestController
@RequestMapping("api/user/v1")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/code")
    public Result<Void> sendCode(@RequestBody UserSendCodeDto userSendCodeDto) {
        String username = userSendCodeDto.getUsername();
        String email = userSendCodeDto.getEmail();
        userService.sendCode(username, email);

        return Result.success();
    }

    @Transactional
    @PostMapping("/register")
    public Result<Void> register(@RequestBody UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String verifyCode = userRegisterDto.getVerifyCode();

        userService.register(username, password, email, verifyCode);

        User user = BeanUtil.copyProperties(userRegisterDto, User.class);

        String encodedPassword = MD5.create().digestHex16(password);
        user.setPassword(encodedPassword);
        user.setRegisterTime(LocalDateTime.now());
        userService.save(user);

        return Result.success();
    }
}
