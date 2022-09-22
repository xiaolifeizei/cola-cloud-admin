package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.common.utils.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

import java.util.Base64;

/**
 * 授权码存放到Redis中
 *
 * @author : cui_feng
 * @since : 2022-09-21 14:12
 */
@AllArgsConstructor
public class RedisAuthorizationCodeServices extends InMemoryAuthorizationCodeServices {

    private final RedisUtil redisUtil;

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redisUtil.set(ColaCacheName.OAUTH2_AUTHORIZATION_CODE.cacheName() + "::" +code,
                Base64.getEncoder().encodeToString(SerializationUtils.serialize(authentication)),
                5 * 60);
    }

    @Override
    public OAuth2Authentication remove(String code) {
        Object authentication = redisUtil.get(ColaCacheName.OAUTH2_AUTHORIZATION_CODE.cacheName() + "::" +code);
        if (ObjectUtil.isNotEmpty(authentication)) {
            redisUtil.del(ColaCacheName.OAUTH2_AUTHORIZATION_CODE.cacheName() + "::" +code);
            return SerializationUtils.deserialize(Base64.getDecoder().decode(authentication.toString()));
        }
        return null;
    }
}
