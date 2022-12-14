package com.matrix.cola.cloud.gateway.handler;

import com.matrix.cola.cloud.gateway.util.ResponseOut;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 异常处理
 *
 * @author : cui_feng
 * @since : 2022-09-05 17:07
 */
public class ErrorExceptionHandler extends DefaultErrorWebExceptionHandler {

	public ErrorExceptionHandler(ErrorAttributes errorAttributes,
								 WebProperties.Resources resources,
                                 ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}

	/**
	 * 获取异常属性
	 */
	@Override
	protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		int code = 500;
		Throwable error = super.getError(request);
		if (error instanceof NotFoundException) {
			code = 404;
		}
		if (error instanceof ResponseStatusException) {
			code = ((ResponseStatusException) error).getStatus().value();
		}

		return ResponseOut.out(code, this.buildMessage(request, error));
	}

	/**
	 * 指定响应处理方法为JSON处理的方法
	 *
	 * @param errorAttributes 异常属性
	 */
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	/**
	 * 根据code获取对应的HttpStatus
	 *
	 * @param errorAttributes 异常属性
	 */
	@Override
	protected int getHttpStatus(Map<String, Object> errorAttributes) {
		return (int) errorAttributes.get("code");
	}

	/**
	 * 构建异常信息
	 *
	 * @param request 请求
	 * @param ex 异常
	 * @return msg
	 */
	private String buildMessage(ServerRequest request, Throwable ex) {
		StringBuilder message = new StringBuilder("Failed to handle request [");
		message.append(request.methodName());
		message.append(" ");
		message.append(request.uri());
		message.append("]");
		if (ex != null) {
			message.append(": ");
			message.append(ex.getMessage());
		}
		return message.toString();
	}



}
