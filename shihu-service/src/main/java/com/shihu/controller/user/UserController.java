package com.shihu.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihu.dto.UserDto;
import com.shihu.dto.UserLoginDto;
import com.shihu.dto.UserRegisterDto;
import com.shihu.dto.UserSendCodeDto;
import com.shihu.entity.User;
import com.shihu.service.UserService;
import com.shihu.util.UserHolder;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shihu.constant.MessageConstant;
import shihu.exception.ClientException;
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
    public Result<Void> sendCode(@RequestBody @Valid UserSendCodeDto userSendCodeDto) {
        String username = userSendCodeDto.getUsername();
        String email = userSendCodeDto.getEmail();
        userService.sendCode(username, email);

        return Result.success();
    }

    @Transactional
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String verifyCode = userRegisterDto.getCode();

        userService.register(username, password, email, verifyCode);

        User user = BeanUtil.copyProperties(userRegisterDto, User.class);

        String encodedPassword = MD5.create().digestHex16(password);
        user.setPassword(encodedPassword);
        user.setRegisterTime(LocalDateTime.now());
        userService.save(user);

        return Result.success();
    }

    @PostMapping("/sendEmail")
    public Result<Void> sendEmail(@RequestBody UserSendCodeDto userSendCodeDto) {
        String email = userSendCodeDto.getEmail();
        userService.sendEmail(email);

        return Result.success();
    }

    @Transactional
    @PostMapping("/resetPassword")
    public Result<Void> resetPassword(@RequestBody UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        String password = userRegisterDto.getPassword();
        String verifyCode = userRegisterDto.getCode();

        userService.resetPassword(email, password, verifyCode);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

        User user = userService.getOne(userLambdaQueryWrapper.eq(User::getEmail,email));
        String encodedPassword = MD5.create().digestHex16(password);
        user.setPassword(encodedPassword);
        userService.updateById(user);

        return Result.success();
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid UserLoginDto userLoginDto){
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        String encodedPassword = MD5.create().digestHex16(password);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        User user = userService.getOne(userLambdaQueryWrapper.eq(User::getUsername, username).eq(User::getPassword, encodedPassword));
        if (BeanUtil.isEmpty(user)){
            throw new ClientException(MessageConstant.WRONG_PASSWORD);
        }

        String token = userService.getToken(user);

        return Result.success(token);
    }

    @GetMapping("/info")
    private Result info(){
        UserDto user = UserHolder.getUser();
        if(BeanUtil.isEmpty(user)){
            throw new ClientException(MessageConstant.TOKEN_EXPIRED);
        }

        return Result.success(user);
    }

    @GetMapping("/logOut")
    private Result userLogOut(){
        // TODO 这里可以做一个优化，删除redis中的token
        return Result.success();
    }
}
