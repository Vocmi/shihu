package com.shihu.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shihu.dto.UserDto;
import com.shihu.dto.UserLoginDto;
import com.shihu.entity.Post;
import com.shihu.entity.User;
import com.shihu.service.PostService;
import com.shihu.service.UserService;
import com.shihu.util.UserHolder;
import com.shihu.vo.ArticleModerationVo;
import com.shihu.vo.PostPageVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shihu.constant.MessageConstant;
import shihu.exception.ClientException;
import shihu.result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-23
 */
@RestController
@RequestMapping("api/admin/v1")
@Slf4j
public class adminController {
    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

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
