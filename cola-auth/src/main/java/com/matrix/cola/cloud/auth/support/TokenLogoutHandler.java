package com.matrix.cola.cloud.auth.support;

import cn.hutool.http.HttpStatus;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.common.utils.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 推出登陆处理器
 *
 * @author : cui_feng
 * @since : 2022-04-21 17:24
 */
public class TokenLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ResponseUtil.out(response, Result.err(HttpStatus.HTTP_OK, "退出登陆成功"));
    }
}
