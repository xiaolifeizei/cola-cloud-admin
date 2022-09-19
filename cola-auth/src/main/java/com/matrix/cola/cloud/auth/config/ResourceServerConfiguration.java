package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.filter.TokenAuthFilter;
import com.matrix.cola.cloud.auth.support.TokenAccessDeniedHandler;
import com.matrix.cola.cloud.auth.support.TokenLogoutHandler;
import com.matrix.cola.cloud.auth.support.TokenUnAuthEntryPint;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Collections;

/**
 * 资源服务器配置
 *
 * @author : cui_feng
 * @since : 2022-09-16 11:45
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 自定义登录成功处理器
     */
    private AuthenticationSuccessHandler loginInSuccessHandler;

    private TokenAuthFilter tokenAuthFilter;

    private AuthenticationManager authenticationManager;

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http.headers().frameOptions().disable();
        http
                .formLogin()
                    .successHandler(loginInSuccessHandler)
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new TokenUnAuthEntryPint())// 未认证
                    .accessDeniedHandler(new TokenAccessDeniedHandler()) // 权限不足
                .and()
                    .cors()
                    .configurationSource(corsConfigurationSource())
                .and()
                    .csrf().disable()//关闭csrf
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭Session
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/oauth/**",
                            "/actuator/**","/token/**", "/mobile/**", "/v2/api-docs", "/v2/api-docs-ext").permitAll()// 允许所有人访问
                        .anyRequest().authenticated() // 其他所有访问需要鉴权认证
                .and()
                    .logout().logoutUrl("/auth/logout")
                    .addLogoutHandler(new TokenLogoutHandler())
                .and()
                    .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 跨域配置
     * @return 跨域配置对象
     */
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
