package com.matrix.cola.cloud.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import java.util.Date;
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
        payload.put(SecurityConst.OAUTH2_APPROVE_ID_KEY,approveId);
        payload.put(SecurityConst.OAUTH2_CLIENT_ID,clientId);
        if (ObjectUtil.isNotEmpty(responseTypes)) {
            payload.put(SecurityConst.OAUTH2_RESPONSE_TYPE, responseTypes.toArray()[0]);
        }

        return JWTUtil.createToken(payload, SecurityConst.APPROVE_JWT_KEY.getBytes());
    }

    /**
     * 判断token类型是否为bearer开头
     * @param token token
     * @return boolean
     */
    public static boolean isBearer(String token) {
        return StrUtil.isNotEmpty(token) && token.toLowerCase().startsWith("bearer ");
    }

    /**
     * 判断token是否过期
     *
     * @param token 前端传过来的Token
     * @return 是否过期
     */
    public static boolean isTokenValid(String token) {

        if (StrUtil.isEmpty(token)) {
            return true;
        }

        if (isBearer(token)) {
            token = token.substring(7);
        }

        // 验证ToKen是否有效
        try {
            if (!JWTUtil.verify(token,SecurityConst.JWT_KEY.getBytes())) {
                return false;
            }
        } catch (Exception ignore) {
            return false;
        }

        JWT jwt = JWTUtil.parseToken(token);
        Object exp = jwt.getPayload("exp");
        if (ObjectUtil.isNull(exp)) {
            return true;
        }
        DateTime expTime = new DateTime(Long.parseLong(exp.toString()));
        return expTime.isBefore(new Date());
    }
}
