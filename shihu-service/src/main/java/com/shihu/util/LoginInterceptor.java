package com.shihu.util;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.shihu.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import shihu.cache.UserCacheKey;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoginInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        System.out.println(token);

        if (StrUtil.isBlank(token)) {
            return false;
        }

        String key  = UserCacheKey.TOKEN.getKey(token);
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        // 3.判断用户是否存在
        if (userMap.isEmpty()) {
            return false;
        }
        // 5.将查询到的hash数据转为UserDTO
        UserDto userDto = BeanUtil.fillBeanWithMap(userMap, new UserDto(), false);
        // 6.存在，保存用户信息到 ThreadLocal
        UserHolder.saveUser(userDto);
        // 7.刷新token有效期
        stringRedisTemplate.expire(key, UserCacheKey.TOKEN.timeout, TimeUnit.MINUTES);
        // 8.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}