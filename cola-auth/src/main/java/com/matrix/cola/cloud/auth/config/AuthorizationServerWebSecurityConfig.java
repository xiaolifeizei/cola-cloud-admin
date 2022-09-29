package com.matrix.cola.cloud.auth.config;


import com.matrix.cola.cloud.api.feign.system.login.LoginServiceFeign;
import com.matrix.cola.cloud.auth.filter.AuthApproveFilter;
import com.matrix.cola.cloud.auth.filter.TokenLoginFilter;
import com.matrix.cola.cloud.auth.service.ClientDetailsServiceImpl;
import com.matrix.cola.cloud.auth.service.SecurityUserDetailsServiceImpl;
import com.matrix.cola.cloud.auth.support.ColaPasswordEncoderFactories;
import com.matrix.cola.cloud.auth.support.RedisAuthorizationCodeServices;
import com.matrix.cola.cloud.auth.support.TokenAccessDeniedHandler;
import com.matrix.cola.cloud.auth.support.TokenUnAuthEntryPint;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * 认证服务器Security配置
 *
 * @author : cui_feng
 * @since : 2022-04-20 14:18
 */
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "spring.application",name="name",havingValue = "cola-auth")
public class AuthorizationServerWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CacheProxy cacheProxy;

    private final TokenUnAuthEntryPint tokenUnAuthEntryPint;

    private final DataSource dataSource;

    private LoginServiceFeign loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return ColaPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 设置授权码模式的授权码如何存取
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(CacheProxy cacheProxy) {
        return new RedisAuthorizationCodeServices(cacheProxy);
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin()
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(new TokenAccessDeniedHandler())
                    .authenticationEntryPoint(tokenUnAuthEntryPint)
            .and()
                .addFilterAt(tokenLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthApproveFilter(cacheProxy), UsernamePasswordAuthenticationFilter.class)
                .csrf()
                    .disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public TokenLoginFilter tokenLoginFilter() {
        return new TokenLoginFilter(authenticationManagerBean());
    }

    @Bean
    public ClientDetailsServiceImpl ClientDetailsServiceImpl() {
        return new ClientDetailsServiceImpl(dataSource);
    }

    @Bean
    public SecurityUserDetailsServiceImpl userDetailsService() {
        return new SecurityUserDetailsServiceImpl(loginService);
    }
}
