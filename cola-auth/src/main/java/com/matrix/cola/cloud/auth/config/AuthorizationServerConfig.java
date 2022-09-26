package com.matrix.cola.cloud.auth.config;

import com.matrix.cola.cloud.auth.endpoint.OAuth2AuthorizationEndpoint;
import com.matrix.cola.cloud.auth.filter.TokenLoginFilter;
import com.matrix.cola.cloud.auth.service.ClientDetailsServiceImpl;
import com.matrix.cola.cloud.auth.support.JwtTokenEnhancer;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权认证器
 *
 * @author : cui_feng
 * @since : 2022-09-14 10:45
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
@ConditionalOnProperty(prefix = "spring.application",name="name",havingValue = "cola-auth")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    private final ClientDetailsServiceImpl clientDetailsService;

    private final TokenStore tokenStore;

    private final CacheProxy cacheProxy;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    private final AuthenticationManager authenticationManager;

    private final AuthorizationCodeServices authorizationCodeServices;

    private TokenLoginFilter tokenLoginFilter;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices);

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(new JwtTokenEnhancer());
        enhancerList.add(jwtAccessTokenConverter);
        tokenEnhancerChain.setTokenEnhancers(enhancerList);
        endpoints.tokenEnhancer(tokenEnhancerChain);

        OAuth2AuthorizationEndpoint oauth2AuthorizationEndpoint = new OAuth2AuthorizationEndpoint();
        oauth2AuthorizationEndpoint.setCacheProxy(cacheProxy);
        oauth2AuthorizationEndpoint.setProviderExceptionHandler(endpoints.getExceptionTranslator());
        oauth2AuthorizationEndpoint.setTokenGranter(endpoints.getTokenGranter());
        oauth2AuthorizationEndpoint.setClientDetailsService(endpoints.getClientDetailsService());
        oauth2AuthorizationEndpoint.setAuthorizationCodeServices(endpoints.getAuthorizationCodeServices());
        oauth2AuthorizationEndpoint.setOAuth2RequestFactory(endpoints.getOAuth2RequestFactory());
        oauth2AuthorizationEndpoint.setOAuth2RequestValidator(endpoints.getOAuth2RequestValidator());
        tokenLoginFilter.setOauth2AuthorizationEndpoint(oauth2AuthorizationEndpoint);
    }
}
