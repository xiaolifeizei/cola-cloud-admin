package com.matrix.cola.cloud.auth.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.feign.system.login.LoginServiceFeign;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权过滤器
 *
 * @author : cui_feng
 * @since : 2022-04-21 16:36
 */
@AllArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    LoginServiceFeign loginService;

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

        if (StrUtil.isEmpty(token)) {
            token = WebUtil.getToken();
        }

        // token过期
        if (StrUtil.isEmpty(token) || WebUtil.isTokenExp(token)) {
            return null;
        }

        UserEntity userPO = null;

        // 解析token
        JWT jwt = JWTUtil.parseToken(token);
        Object approveId = jwt.getPayload("approveId");
        if (ObjectUtil.isNotEmpty(approveId)) {
            userPO = cacheProxy.getEvictObject(ColaCacheName.OAUTH2_APPROVE_ID, approveId.toString());
        }

        // 获取当前登陆用户
        userPO = ObjectUtil.isEmpty(userPO) ? WebUtil.getUser() : userPO;
        if (ObjectUtil.isNull(userPO)) {
            return null;
        }

        // 获取当前用户的角色编码
        List<String> roleCodeList = loginService.getUserRoleCodeList(userPO.getId());
        if (ObjectUtil.isEmpty(roleCodeList)) {
            roleCodeList = new ArrayList<>();
            roleCodeList.add(ColaConstant.DEFAULT_ROLE_CODE);
        }

        // 生成权限列表
        List<GrantedAuthority> permissionList = new ArrayList<>();
        for (String role : roleCodeList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
            permissionList.add(simpleGrantedAuthority);
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUser(userPO);
        securityUser.setPermissionList(roleCodeList);

        return new UsernamePasswordAuthenticationToken(securityUser,token,permissionList);
    }
}
