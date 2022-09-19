package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import com.matrix.cola.cloud.auth.utils.JwtTokenUtil;
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
        info.put("client_id", JwtTokenUtil.getClientId());
        info.put("id",StrUtil.toString(principal.getCurrentUser().getId()));
        info.put("name",StrUtil.emptyToDefault(principal.getCurrentUser().getName(),""));
        info.put("loginName",StrUtil.emptyToDefault(principal.getCurrentUser().getLoginName(),""));
        info.put("groupId",principal.getCurrentUser().getGroupId());

        // 计算过期时间
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.MINUTE, JwtTokenUtil.EXPIRATION);

        info.put(JWTPayload.ISSUED_AT, now.getTime()); //签发时间
        info.put(JWTPayload.EXPIRES_AT, expTime.getTime()); //过期时间
        info.put(JWTPayload.NOT_BEFORE, now.getTime()); //生效时间

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        ((DefaultOAuth2AccessToken) accessToken).setExpiration(expTime);

        return accessToken;
    }
}
