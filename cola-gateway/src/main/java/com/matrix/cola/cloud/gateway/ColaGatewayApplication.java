package com.matrix.cola.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关启动
 *
 * @author : cui_feng
 * @since : 2022-08-31 17:26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class ColaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColaGatewayApplication.class, args);
    }
}
