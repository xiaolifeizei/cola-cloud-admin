package com.matrix.cola.cloud.auth.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.utils.SecurityConst;
import com.matrix.cola.cloud.common.utils.WebUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * 创建和验证Jwt
 *
 * @author : cui_feng
 * @since : 2022-04-21 10:30
 */
public class JwtTokenUtil {

    /**
     * 多长时间超时，单位是分钟
     */
    public static final Integer EXPIRATION = 3 * 60;

    /**
     * 生成jwtToken
     * @param userPO 用户
     * @param expMinute 超时时间
     * @return JWT字符串
     */
    private static String createToken(UserEntity userPO, Integer expMinute) {

        if (ObjectUtil.isNull(userPO)) return null;
        if (ObjectUtil.isNull(expMinute) || expMinute <= 0) {
            expMinute = EXPIRATION;
        }

        // 计算过期时间
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.MINUTE, expMinute);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(JWTPayload.ISSUED_AT, now.getTime()); //签发时间
        payload.put(JWTPayload.EXPIRES_AT, expTime.getTime()); //过期时间
        payload.put(JWTPayload.NOT_BEFORE, now.getTime()); //生效时间

        payload.put("id",userPO.getId() == null ? "" : userPO.getId().toString());
        payload.put("name",StrUtil.emptyToDefault(userPO.getName(),""));
        payload.put("loginName",StrUtil.emptyToDefault(userPO.getLoginName(),""));
        payload.put("groupId",userPO.getGroupId());

        return JWTUtil.createToken(payload, SecurityConst.JWT_KEY.getBytes());
    }

    /**
     * 创建Token
     * @param userPO 用户
     * @return JWT字符串
     */
    public static String createToken(UserEntity userPO){
        return createToken(userPO,EXPIRATION);
    }

    /**
     * 从Header中获取token
     * @return token数组
     */
    private static String[] getTokenFromHeader() {
        String header = WebUtil.getRequest().getHeader(SecurityConst.TOKEN_KEY);
        if (header == null || !header.startsWith(SecurityConst.TOKEN_PREFIX)) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        byte[] base64Token = header.substring(5).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int index = token.indexOf("#");
        if (index == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取客户端id
     * @return ClientId
     */
    public static String getClientId () {
        return getTokenFromHeader()[0];
    }

    public static String getToken() {
        return getTokenFromHeader()[1];
    }
}
