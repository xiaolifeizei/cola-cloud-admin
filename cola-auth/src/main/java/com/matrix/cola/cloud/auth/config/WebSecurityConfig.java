package com.matrix.cola.cloud.auth.config;


import com.matrix.cola.cloud.auth.support.ColaPasswordEncoderFactories;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Web安全配置
 *
 * @author : cui_feng
 * @since : 2022-04-20 14:18
 */

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return ColaPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
