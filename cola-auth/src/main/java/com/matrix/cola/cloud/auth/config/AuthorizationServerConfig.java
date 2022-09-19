package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.service.ClientDetailsServiceImpl;
import com.matrix.cola.cloud.auth.service.SecurityUserDetailsServiceImpl;
import com.matrix.cola.cloud.auth.support.JwtTokenEnhancer;
import com.matrix.cola.cloud.auth.utils.AuthConstant;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 授权认证器
 *
 * @author : cui_feng
 * @since : 2022-09-14 10:45
 */
@Order
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    private TokenStore tokenStore;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private JwtTokenEnhancer tokenEnhancer;

    private AuthenticationManager authenticationManager;

    private SecurityUserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        ClientDetailsServiceImpl clientService = new ClientDetailsServiceImpl(dataSource);
        clientService.setSelectClientDetailsSql(AuthConstant.DEFAULT_SELECT_STATEMENT);
        clientService.setFindClientDetailsSql(AuthConstant.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        // 增加过滤器链
        if (jwtAccessTokenConverter != null && tokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancerList = new ArrayList<>();
            enhancerList.add(tokenEnhancer);
            enhancerList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(enhancerList);

            endpoints.tokenEnhancer(tokenEnhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }
}
