package com.matrix.cola.cloud.auth.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 用户确认授权验证过滤器
 *
 * @author : cui_feng
 * @since : 2022-04-21 16:36
 */
@AllArgsConstructor
public class AuthApproveFilter extends OncePerRequestFilter {

    CacheProxy cacheProxy;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws IOException, ServletException {
        // 获取当前登陆用户的权限信息
        UsernamePasswordAuthenticationToken authToken = getAuthentication(request);
        // 放到security上下文中
        if (ObjectUtil.isNotNull(authToken)) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = WebUtil.getApproveToken();

        // token过期
        if (StrUtil.isEmpty(token) || WebUtil.isTokenExp(token)) {
            return null;
        }

        // 解析token
        JWT jwt = JWTUtil.parseToken(token);
        Object approveId = jwt.getPayload("approveId");
        UserEntity userPO = cacheProxy.getEvictObject(ColaCacheName.OAUTH2_APPROVE_ID, approveId.toString());

        if (ObjectUtil.isNull(userPO)) {
            return null;
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUser(userPO);

        return new UsernamePasswordAuthenticationToken(securityUser,token,new ArrayList<>());
    }
}
