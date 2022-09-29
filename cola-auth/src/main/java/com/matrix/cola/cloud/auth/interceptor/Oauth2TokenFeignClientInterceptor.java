package com.matrix.cola.cloud.auth.interceptor;

import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.common.utils.WebUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign客户端请求时自动添加Token
 *
 * @author : cui_feng
 * @since : 2022-09-29 15:30
 */
public class Oauth2TokenFeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = WebUtil.getRequest();
        String authorization = request == null ? null : request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotEmpty(authorization)) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
        }
    }
}
