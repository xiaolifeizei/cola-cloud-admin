package com.matrix.cola.cloud.auth.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.feign.system.login.LoginServiceFeign;
import com.matrix.cola.cloud.auth.utils.JwtTokenUtil;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.AllArgsConstructor;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取当前登陆用户的权限信息
        UsernamePasswordAuthenticationToken authToken = getAuthentication(request);
        // 放到security上下文中
        if (ObjectUtil.isNotNull(authToken)) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = JwtTokenUtil.getToken();

        // token过期
        if (StrUtil.isEmpty(token) || WebUtil.isTokenExp(token)) {
            return null;
        }

        // 获取当前登陆用户
        UserEntity userPO = WebUtil.getUser();
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

        return new UsernamePasswordAuthenticationToken(userPO.getLoginName(),token,permissionList);
    }
}
