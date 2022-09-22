package com.matrix.cola.cloud.auth.support;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.common.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过的用户访问无权限资源时的异常
 *
 * @author : cui_feng
 * @since : 2022-04-22 15:01
 */
public class TokenAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtil.out(response, Result.err(response.getStatus(),"权限不足，您没有权限访问本资源,"+ request.getRequestURI()));
    }
}
