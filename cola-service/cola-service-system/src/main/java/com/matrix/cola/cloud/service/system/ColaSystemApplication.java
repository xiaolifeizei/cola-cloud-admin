package com.matrix.cola.cloud.service.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统服务
 *
 * @author : cui_feng
 * @since : 2022-09-02 14:21
 */
@SpringBootApplication
@EnableFeignClients("com.matrix.cola.cloud")
@MapperScan("com.matrix.cola.cloud.service.**.mapper")
public class ColaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColaSystemApplication.class,args);
    }
}
