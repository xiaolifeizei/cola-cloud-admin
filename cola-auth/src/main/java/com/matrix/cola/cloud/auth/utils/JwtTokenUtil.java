package com.matrix.cola.cloud.auth.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.common.utils.SecurityConst;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.util.HashMap;
import java.util.Set;

/**
 * 创建和验证Jwt
 *
 * @author : cui_feng
 * @since : 2022-04-21 10:30
 */
public class JwtTokenUtil {

    /**
     * 多长时间超时，单位是秒
     */
    public static final Integer EXPIRATION = 3 * 60;

    /**
     * 生成token
     * @return JWT字符串
     */
    public static String createApproveToken(String approveId, String clientId, Set<String> responseTypes) {

        // 计算过期时间
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.SECOND, EXPIRATION);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(JWTPayload.ISSUED_AT, now.getTime()); //签发时间
        payload.put(JWTPayload.EXPIRES_AT, expTime.getTime()); //过期时间
        payload.put(JWTPayload.NOT_BEFORE, now.getTime()); //生效时间
        payload.put("approveId",approveId);
        payload.put(OAuth2Utils.CLIENT_ID,clientId);
        if (ObjectUtil.isNotEmpty(responseTypes)) {
            payload.put(OAuth2Utils.RESPONSE_TYPE, responseTypes.toArray()[0]);
        }

        return JWTUtil.createToken(payload, SecurityConst.APPROVE_JWT_KEY.getBytes());
    }

}
