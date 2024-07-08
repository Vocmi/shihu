package com.shihu.config;


import com.shihu.util.LoginInterceptor;
import com.shihu.util.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(stringRedisTemplate))
                .excludePathPatterns(
                        "/api/user/v1/register",
                        "/api/user/v1/code",
                        "/api/user/v1/sendEmail",
                        "/api/user/v1/resetPassword",
                        "/api/user/v1/login",
                        "/api/admin/v1/login"
                );
    }
}
