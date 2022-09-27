package com.matrix.cola.cloud.auth.filter;

import com.alibaba.nacos.common.utils.HttpMethod;
import com.matrix.cola.cloud.auth.properties.AuthProperties;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 动态权限过滤器
 *
 * @author : cui_feng
 * @since : 2022-09-27 09:18
 */
@AllArgsConstructor
public class DynamicSecurityFilter extends OncePerRequestFilter {

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    private final AuthProperties authProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        //OPTIONS请求直接放行
        if(request.getMethod().equals(HttpMethod.OPTIONS)){
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            chain.doFilter(request,response);
            return;
        }
        //白名单请求直接放行
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String path : authProperties.getSkipUrl()) {
            if(pathMatcher.match(path,request.getRequestURI())){
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                chain.doFilter(request,response);
                return;
            }
        }
        chain.doFilter(request,response);
    }
}
