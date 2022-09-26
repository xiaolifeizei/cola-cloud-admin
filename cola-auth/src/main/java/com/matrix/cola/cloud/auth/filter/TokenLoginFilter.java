package com.matrix.cola.cloud.auth.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.auth.endpoint.OAuth2AuthorizationEndpoint;
import com.matrix.cola.cloud.common.utils.ResponseUtil;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 认证过滤器
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
@Setter
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private OAuth2AuthorizationEndpoint oauth2AuthorizationEndpoint;

    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        super.setPostOnly(true);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/oauth2/authorize"));
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

        if (SecurityContextHolder.getContext().getAuthentication()!=null) {
            return SecurityContextHolder.getContext().getAuthentication();
        }

        UserEntity userPO = new UserEntity();
        userPO.setLoginName(request.getParameter("loginName"));
        userPO.setPassword(request.getParameter("password"));

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userPO.getLoginName(),userPO.getPassword(),new ArrayList<>()));
    }

    /**
     * 认证成功后
     * @param request request对象
     * @param response response对象
     * @param chain 过滤器链
     * @param authResult 认证请求
     * @throws IOException IO异常
     * @throws ServletException Web异常
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }


        HashMap<String,String> paramMap = new HashMap<>(16);
        request.getParameterMap().forEach((k, v) -> paramMap.put(k,v[0]));

        String approveToken = WebUtil.getApproveToken();
        if (StrUtil.isNotEmpty(approveToken)) {
            JWT jwt = JWTUtil.parseToken(approveToken);
            Object clientId = jwt.getPayload(OAuth2Utils.CLIENT_ID);
            Object responseType = jwt.getPayload(OAuth2Utils.RESPONSE_TYPE);
            paramMap.put(OAuth2Utils.CLIENT_ID, ObjectUtil.isEmpty(clientId) ? null : clientId.toString());
            paramMap.put(OAuth2Utils.RESPONSE_TYPE, ObjectUtil.isEmpty(responseType) ? null : responseType.toString());
        }

        if (paramMap.containsKey(OAuth2Utils.USER_OAUTH_APPROVAL)) {
            ResponseUtil.out(response,oauth2AuthorizationEndpoint.approveOrDeny(paramMap,context.getAuthentication()));
        } else {
            ResponseUtil.out(response,oauth2AuthorizationEndpoint.oauth2Authorize(paramMap, context.getAuthentication()));
        }
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
