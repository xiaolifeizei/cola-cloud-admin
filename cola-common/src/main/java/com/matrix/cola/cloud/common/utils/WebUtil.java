package com.matrix.cola.cloud.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Web工具类
 *
 * @author : cui_feng
 * @since : 2022-05-05 17:10
 */
public class WebUtil {

    /**
     * 存入到request对象中的Key
     */
    public static final String COLA_USER_REQUEST_ATTR = "COLA_USER_REQUEST_ATTR_";

    /**
     * IP头信息
     */
    private static final String[] IP_HEADER_NAMES = new String[]{
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 验证IP地址不为空不为unknown
     */
    private static final Predicate<String> IP_PREDICATE = (ip) -> StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip);


    /**
     * 获取IP地址
     * @return ip地址
     */
    public static String getIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        String ip = null;
        for (String ipHeader : IP_HEADER_NAMES) {
            ip = request.getHeader(ipHeader);
            if (!IP_PREDICATE.test(ip)) {
                break;
            }
        }
        // header中没有则取remoteAddr
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StrUtil.isNotEmpty(ip) && ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }

        return StrUtil.isBlank(ip) ? null : StrUtil.splitTrim(ip, ',').get(0);
    }

    /**
     * 获取Request对象
     * @return request对象
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取当前登陆用户
     * @return 用户实体类
     */
    public static UserEntity getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从 request 中获取
        Object user = request.getAttribute(COLA_USER_REQUEST_ATTR);
        if (user == null) {
            user = getUser(request);
            if (user != null) {
                // 设置到 request 中
                request.setAttribute(COLA_USER_REQUEST_ATTR, user);
            }
        }
        return (UserEntity) user;
    }

    public static String getToken() {
        String token = Objects.requireNonNull(WebUtil.getRequest()).getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(token)) {
            return null;
        }

        // bearer check
        if (!token.toLowerCase().startsWith("bearer ")) {
            return null;
        }

        return token.substring(7);
    }

    public static String getApproveToken() {
        return Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecurityConst.OAUTH2_APPROVE_TOKEN);
    }

    /**
     * 从token中解析当前用户
     * @param request request对象
     * @return 用户实体类
     */
    private static UserEntity getUser(HttpServletRequest request) {

        String token = getToken();

        // token过期
        if (StrUtil.isEmpty(token) || JwtTokenUtil.isTokenValid(token)) {
            return null;
        }

        // 解析token
        JWT jwt = JWTUtil.parseToken(token);
        Object id = jwt.getPayload("id");

        // 解析不出id直接返回
        if (ObjectUtil.isNull(id)) return null;

        Object name = jwt.getPayload("name");
        Object loginName = jwt.getPayload("loginName");
        Object phone = jwt.getPayload("phone");
        Object groupId = jwt.getPayload("groupId");

        // 填充值
        UserEntity user = new UserEntity();
        user.setId(ObjectUtil.isNull(id)?null:Long.valueOf(id.toString()));
        user.setName(ObjectUtil.isNull(name)?"":name.toString());
        user.setLoginName(ObjectUtil.isNull(loginName)?"":loginName.toString());
        user.setPhone(ObjectUtil.isNull(phone)?"":phone.toString());
        user.setGroupId(ObjectUtil.isNull(groupId)?null:groupId.toString());

        return user;
    }

    /**
     * 判断当前用户是否为超管
     * @return boolean
     */
    public static boolean isAdministrator() {
        UserEntity userPO = getUser();
        if (ObjectUtil.isNull(userPO) || ObjectUtil.isNull(userPO.getId())) {
            return false;
        }
        CacheProxy cacheProxy = null;
        List<Long> roleIds = null;
        try {
            cacheProxy = SpringUtil.getBean(CacheProxy.class);
            if (ObjectUtil.isNotNull(cacheProxy)) {
                roleIds = cacheProxy.getObject(ColaCacheName.USER_ROLE_IDS,userPO.getId().toString());
            } else {
                return false;
            }
        } catch (Exception ignored) {
        }

        if (roleIds == null) {
            return false;
        }
        return roleIds.contains(ColaConstant.ADMINISTRATOR_ID);
    }
}
