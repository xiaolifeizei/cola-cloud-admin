package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

        SecurityUser user;
        Map<String, Object> info = new HashMap<>(16);
        info.put("clientId", authentication.getOAuth2Request().getClientId());

        // 客户端模式
        if ("client_credentials".equals(authentication.getOAuth2Request().getGrantType())) {
            user = new SecurityUser();
            UserEntity userEntity = new UserEntity();
            userEntity.setId(0L);
            userEntity.setName(authentication.getOAuth2Request().getClientId());
            userEntity.setLoginName(authentication.getOAuth2Request().getClientId());
            userEntity.setGroupId("0");
            user.setCurrentUser(userEntity);
        } else {
            user = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
        }

        info.put("id",StrUtil.toString(user.getCurrentUser().getId()));
        info.put("name",StrUtil.emptyToDefault(user.getCurrentUser().getName(),""));
        info.put("loginName",StrUtil.emptyToDefault(user.getCurrentUser().getLoginName(),""));
        info.put("groupId",user.getCurrentUser().getGroupId());
        DateTime now = DateTime.now();
        info.put("iat", now.getTime() / 1000); //签发时间，单位为秒（与exp一致）
        info.put("success",true);
        info.put("msg", "操作成功！");
        info.put("code",200);
        info.put("data", new LinkedHashMap<>());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
