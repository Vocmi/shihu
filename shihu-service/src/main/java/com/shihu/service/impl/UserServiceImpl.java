package com.shihu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.dto.UserDto;
import com.shihu.entity.User;
import com.shihu.mapper.UserMapper;
import com.shihu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import shihu.cache.UserCacheKey;
import shihu.constant.EmailConstant;
import shihu.constant.MessageConstant;
import shihu.exception.ClientException;
import com.shihu.util.SendMailUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

    @Override
    public void sendEmail(String email) {
        if (isNotEmail(email)) {
            throw new ClientException(MessageConstant.EMAIL_INVALID);
        }

        if (isNotEmailExists(email)) {
            throw new ClientException(MessageConstant.EMAIL_NOT_REGISTER);
        }

        String code = RandomUtil.randomNumbers(6);
        sendMailUtil.sendSimpleMail(EmailConstant.MESSAGE_HEADER, code, EmailConstant.SENDER, email);

        String key = UserCacheKey.EMAIL_CODE.getKey(email, code);
        stringRedisTemplate.opsForValue().set(key, code, UserCacheKey.EMAIL_CODE.timeout, UserCacheKey.EMAIL_CODE.unit);
    }

    @Override
    public void resetPassword(String email, String password, String verifyCode) {
        if (isNotEmail(email)) {
            throw new ClientException(MessageConstant.EMAIL_INVALID);
        }

        String key = UserCacheKey.EMAIL_CODE.getKey(email, verifyCode);
        if (isNotKeyExist(key)) {
            throw new ClientException(MessageConstant.CODE_INVALID);
        }
    }

    @Override
    public String getToken(User user) {
        // 保存用户到redis
        // 随机生成token作为令牌
        String token = UUID.randomUUID().toString(true);
        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        userDto.setUserPicture( "https://img2.baidu.com/it/u=792942531,3683176324&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313");
        //将User对象转为HashMap存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userDto, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        String tokenKey = UserCacheKey.TOKEN.getKey(token);
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, UserCacheKey.TOKEN.timeout, TimeUnit.MINUTES);

        return token;
    }

    private Boolean isKeyExist(String key) {
        return stringRedisTemplate.delete(key);
    }

    private Boolean isNotKeyExist(String key) {
        return !isKeyExist(key);
    }

    public boolean isNotEmail(String email) {
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

    public boolean isNotEmailExists(String email) {
        return !isEmailExists(email);
    }
}




