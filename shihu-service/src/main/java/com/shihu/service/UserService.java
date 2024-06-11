package com.shihu.service;

import com.shihu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 26867
* @description 针对表【User(用户信息表)】的数据库操作Service
* @createDate 2024-06-11 19:44:00
*/
public interface UserService extends IService<User> {
    void sendCode(String username, String email);

    void register(String username, String password, String email, String verifyCode);
}
