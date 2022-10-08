package com.matrix.cola.cloud.auth.support;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.common.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 匿名用户访问需要权限才能访问的资源时的异常
 *
 * @author : cui_feng
 * @since : 2022-04-21 17:17
 */
public class TokenUnAuthEntryPint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String msg = "认证失败,错误信息：";
        Object e = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (ObjectUtil.isNotNull(e) && e instanceof Exception) {
            msg += ((Exception) e).getMessage();
        } else {
            msg += authException.getMessage();
        }
        ResponseUtil.out(response, Result.err(401, msg));
    }
}
