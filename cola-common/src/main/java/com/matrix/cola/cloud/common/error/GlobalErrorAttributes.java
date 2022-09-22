
package com.matrix.cola.cloud.common.error;

import cn.hutool.core.bean.BeanUtil;
import com.matrix.cola.cloud.api.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 全局异常处理
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		String requestUri = this.getAttr(webRequest, "javax.servlet.error.request_uri");
		Integer status = this.getAttr(webRequest, "javax.servlet.error.status_code");
		Throwable error = getError(webRequest);
		Result result;
		if (error == null) {
			if (status != null && status == 404) {
				result = Result.err(status,String.format("请求的资源不存在：URL:%s 状态码:%d", requestUri, status));
			} else if (status != null && status == 401) {
				result = Result.err(status,String.format("您无权访问该资源：URL:%s 状态码:%d", requestUri, status));
			} else {
				log.error("URL:{} error status:{}", requestUri, status);
				result = Result.err(status,"系统未知异常[HttpStatus]:" + status);
			}
		} else {
			result = Result.err(status == null ? 500: status,error.getMessage());
		}
		return BeanUtil.beanToMap(result);
	}

	@Nullable
	private <T> T getAttr(WebRequest webRequest, String name) {
		return (T) webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

}
