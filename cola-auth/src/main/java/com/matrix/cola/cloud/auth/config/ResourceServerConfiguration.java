package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.support.TokenAccessDeniedHandler;
import com.matrix.cola.cloud.auth.support.TokenUnAuthEntryPint;
import com.matrix.cola.cloud.common.utils.EnvUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
@ConditionalOnMissingBean(AuthorizationServerConfig.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .accessDeniedHandler(new TokenAccessDeniedHandler())
                .resourceId(EnvUtil.getEnvValue("spring.application.name"))
                .tokenStore(tokenStore)
                .accessDeniedHandler(new TokenAccessDeniedHandler())
                .stateless(true);
    }

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http
            .httpBasic()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new TokenUnAuthEntryPint())// 未认证
            .and()
                .cors()
                    .configurationSource(corsConfigurationSource())
            .and()
                .csrf()
                    .disable()// 关闭csrf
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭Session
            .and()
                .authorizeRequests()
                    .antMatchers("/login/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated();// 其他所有访问需要鉴权认证
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
