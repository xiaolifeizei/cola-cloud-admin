package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.common.cache.CacheProxy;
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

    private final CacheProxy cacheProxy;

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        cacheProxy.put(ColaCacheName.OAUTH2_AUTHORIZATION_CODE, code, Base64.getEncoder().encodeToString(SerializationUtils.serialize(authentication)), 5 * 60);

    }

    @Override
    public OAuth2Authentication remove(String code) {
        String authentication = cacheProxy.getEvictObject(ColaCacheName.OAUTH2_AUTHORIZATION_CODE, code);
        if (StrUtil.isNotEmpty(authentication)) {
            return SerializationUtils.deserialize(Base64.getDecoder().decode(authentication));
        }
        return null;
    }
}
