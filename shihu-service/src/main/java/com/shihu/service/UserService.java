package com.shihu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihu.entity.User;

/**
* @author 26867
* @description 针对表【User(用户信息表)】的数据库操作Service
* @createDate 2024-06-11 19:44:00
*/
public interface UserService extends IService<User> {
    boolean isNotEmail(String email);

    boolean isNotEmailExists(String email) ;

    void sendCode(String username, String email);

    void register(String username, String password, String email, String verifyCode);

    void sendEmail(String email);

    void resetPassword(String email, String password, String verifyCode);

    String getToken(User user);
}
