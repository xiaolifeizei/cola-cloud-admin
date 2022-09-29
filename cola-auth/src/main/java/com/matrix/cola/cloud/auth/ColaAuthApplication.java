package com.matrix.cola.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 鉴权服务启动类
 *
 * @author : cui_feng
 * @since : 2022-09-16 18:22
 */
@SpringBootApplication
public class ColaAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColaAuthApplication.class,args);
    }
}
