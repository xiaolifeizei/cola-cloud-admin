
package com.matrix.cola.cloud.common.error;

import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.Servlet;


/**
 * 全局异常处理，处理可预见的异常，优先级高
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleError(MissingServletRequestParameterException e) {
		log.warn("缺少请求参数", e.getMessage());
		String message = String.format("缺少必要的请求参数: %s", e.getParameterName());
		return Result.err(message);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleError(MethodArgumentTypeMismatchException e) {
		log.warn("请求参数格式错误", e.getMessage());
		String message = String.format("请求参数格式错误: %s", e.getName());
		return Result.err(message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleError(MethodArgumentNotValidException e) {
		log.warn("参数验证失败", e.getMessage());
		return handleError(e.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleError(BindException e) {
		log.warn("参数绑定失败", e.getMessage());
		return handleError(e.getBindingResult());
	}

	private Result handleError(BindingResult result) {
		FieldError error = result.getFieldError();
		String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
		return Result.err(message);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Result handleError(NoHandlerFoundException e) {
		log.error("404没找到请求:{}", e.getMessage());
		return Result.err("404没找到请求:"+e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleError(HttpMessageNotReadableException e) {
		log.error("消息不能读取:{}", e.getMessage());
		return Result.err("消息不能读取:"+e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Result handleError(HttpRequestMethodNotSupportedException e) {
		log.error("不支持当前请求方法:{}", e.getMessage());
		return Result.err("不支持当前请求方法: " + e.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public Result handleError(HttpMediaTypeNotSupportedException e) {
		log.error("不支持当前媒体类型:{}", e.getMessage());
		return Result.err("不支持当前媒体类型:" + e.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public Result handleError(HttpMediaTypeNotAcceptableException e) {
		String message = e.getMessage() + " " + StrUtil.join(",",e.getSupportedMediaTypes());
		log.error("不接受的媒体类型:{}", message);
		return Result.err("不接受的媒体类型:"+message);
	}

}
