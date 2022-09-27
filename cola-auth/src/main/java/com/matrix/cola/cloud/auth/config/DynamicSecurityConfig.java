package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.filter.DynamicSecurityFilter;
import com.matrix.cola.cloud.auth.properties.AuthProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态权限配置
 *
 * @author : cui_feng
 * @since : 2022-09-27 10:18
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties
public class DynamicSecurityConfig {

    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter() {
        return new DynamicSecurityFilter(authProperties());
    }

    @Bean
    @RefreshScope
    public AuthProperties authProperties() {
        return new AuthProperties();
    }
}
