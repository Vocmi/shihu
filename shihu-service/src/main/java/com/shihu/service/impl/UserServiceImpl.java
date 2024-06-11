package com.shihu.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.User;
import com.shihu.service.UserService;
import com.shihu.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import shihu.cache.UserCacheKey;
import shihu.constant.EmailConstant;
import shihu.constant.MessageConstant;
import shihu.exception.ClientException;
import com.shihu.util.SendMailUtil;


/**
 * @author 26867
 * @description 针对表【User(用户信息表)】的数据库操作Service实现
 * @createDate 2024-06-11 19:44:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SendMailUtil sendMailUtil;

    @Override
    public void sendCode(String username, String email) {
        if (isNotEmail(email)) {
            throw new ClientException(MessageConstant.EMAIL_INVALID);
        }

        boolean isUserExist = isUserExists(username, email);
        if (isUserExist) {
            throw new ClientException(MessageConstant.USER_EXISTS);
        }

        String code = RandomUtil.randomNumbers(6);
        sendMailUtil.sendSimpleMail(EmailConstant.MESSAGE_HEADER, code, EmailConstant.SENDER, email);

        String key = UserCacheKey.EMAIL_CODE.getKey(email, code);
        stringRedisTemplate.opsForValue().set(key, code, UserCacheKey.EMAIL_CODE.timeout, UserCacheKey.EMAIL_CODE.unit);
    }

    @Override
    public void register(String username, String password, String email, String verifyCode) {
        if (isNotEmail(email)) {
            throw new ClientException(MessageConstant.EMAIL_INVALID);
        }

        boolean isUserExist = isUserExists(username, verifyCode);
        if (isUserExist) {
            throw new ClientException(MessageConstant.USER_EXISTS);
        }

        String key = UserCacheKey.EMAIL_CODE.getKey(email, verifyCode);
        if (isNotKeyExist(key)) {
            throw new ClientException(MessageConstant.CODE_INVALID);
        }
    }

    private Boolean isKeyExist(String key) {
        return stringRedisTemplate.delete(key);
    }

    private Boolean isNotKeyExist(String key) {
        return !isKeyExist(key);
    }

    private Boolean isNotEmail(String email) {
        return !Validator.isEmail(email);
    }

    public boolean isUserExists(String username, String email) {
        return isUsernameExists(username) || isEmailExists(email);
    }

    public boolean isUsernameExists(String username) {
        boolean isExists = lambdaQuery()
                .eq(User::getUsername, username)
                .exists();

        return isExists;
    }

    public boolean isEmailExists(String email) {
        boolean isExists = lambdaQuery()
                .eq(User::getEmail, email)
                .exists();

        return isExists;
    }
}




