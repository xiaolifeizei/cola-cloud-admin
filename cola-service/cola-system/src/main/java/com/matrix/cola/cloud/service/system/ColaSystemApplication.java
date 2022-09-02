package com.matrix.cola.cloud.service.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 系统服务
 *
 * @author : cui_feng
 * @since : 2022-09-02 14:21
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ColaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColaSystemApplication.class,args);
    }
}
