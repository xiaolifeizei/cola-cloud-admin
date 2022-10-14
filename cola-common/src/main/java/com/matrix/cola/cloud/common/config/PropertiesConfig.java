package com.matrix.cola.cloud.common.config;

import com.matrix.cola.cloud.common.properties.AuthProperties;
import com.matrix.cola.cloud.common.properties.DataScopeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件配置
 *
 * @author : cui_feng
 * @since : 2022-09-29 16:40
 */
@Configuration
@EnableConfigurationProperties
public class PropertiesConfig {

    @Bean
    @RefreshScope
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

    @Bean
    @RefreshScope
    public DataScopeProperties dataScopeProperties() {
        return new DataScopeProperties();
    }
}
