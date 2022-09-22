package com.matrix.cola.cloud.auth.config;


import com.matrix.cola.cloud.auth.service.SecurityUserDetailsServiceImpl;
import com.matrix.cola.cloud.auth.support.ColaPasswordEncoderFactories;
import com.matrix.cola.cloud.auth.support.RedisAuthorizationCodeServices;
import com.matrix.cola.cloud.common.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

/**
 * Web安全配置
 *
 * @author : cui_feng
 * @since : 2022-04-20 14:18
 */

@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "spring.application",name="name",havingValue = "cola-auth")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final SecurityUserDetailsServiceImpl userDetailsService;

    private final RedisUtil redisUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return ColaPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 设置授权码模式的授权码如何存取
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new RedisAuthorizationCodeServices(redisUtil);
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
                .antMatchers("/oauth/**").authenticated()
                .anyRequest().permitAll()
            .and()
                .formLogin()
            .and()
                .csrf()
                    .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
