package com.matrix.cola.cloud.auth.filter;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.common.utils.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {


    public TokenLoginFilter() {
        super.setPostOnly(false);
        // super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/oauth/authorize"));
    }

    /**
     * 登陆认证
     * @param request request对象
     * @param response response对象
     * @return 认证对象
     * @throws AuthenticationException 认证异常
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return null;
    }


    /**
     * 登陆失败后
     * @param request request对象
     * @param response response对象
     * @param failed 认证失败对象
     * @throws IOException IO异常
     * @throws ServletException Web异常
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response,Result.err("认证失败!用户名或密码错误"));
    }
}
