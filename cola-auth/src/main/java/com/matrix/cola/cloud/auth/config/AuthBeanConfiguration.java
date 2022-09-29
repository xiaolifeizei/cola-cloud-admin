package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.support.TokenUnAuthEntryPint;
import com.matrix.cola.cloud.common.utils.SecurityConst;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * TokenStore配置
 *
 * @author : cui_feng
 * @since : 2022-09-16 14:03
 */
@Configuration
@AllArgsConstructor
public class AuthBeanConfiguration {

    /**
     * 使用jwtTokenStore存储token
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 用于生成jwt
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(SecurityConst.JWT_KEY);
        return accessTokenConverter;
    }

    @Bean
    public TokenUnAuthEntryPint tokenUnAuthEntryPint() {
        return new TokenUnAuthEntryPint();
    }
}
