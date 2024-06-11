package com.shihu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-10
 */
@SpringBootApplication
@MapperScan("com.shihu.mapper")
public class ShiHuApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiHuApplication.class,args);
    }
}
