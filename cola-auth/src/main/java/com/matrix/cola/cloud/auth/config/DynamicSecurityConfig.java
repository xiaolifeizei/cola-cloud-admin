package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.filter.DynamicSecurityFilter;
import com.matrix.cola.cloud.common.properties.AuthProperties;
import lombok.AllArgsConstructor;
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
public class DynamicSecurityConfig {

    private final AuthProperties authProperties;

    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter() {
        return new DynamicSecurityFilter(authProperties);
    }

}
