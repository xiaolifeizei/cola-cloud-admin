package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken增强器
 *
 * @author : cui_feng
 * @since : 2022-09-16 14:14
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        SecurityUser principal = (SecurityUser) authentication.getUserAuthentication().getPrincipal();

        Map<String, Object> info = new HashMap<>(16);
        info.put("client_id", authentication.getOAuth2Request().getClientId());
        info.put("id",StrUtil.toString(principal.getCurrentUser().getId()));
        info.put("name",StrUtil.emptyToDefault(principal.getCurrentUser().getName(),""));
        info.put("loginName",StrUtil.emptyToDefault(principal.getCurrentUser().getLoginName(),""));
        info.put("groupId",principal.getCurrentUser().getGroupId());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
