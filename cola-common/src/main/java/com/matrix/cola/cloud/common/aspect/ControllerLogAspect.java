/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package com.matrix.cola.cloud.common.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import com.matrix.cola.cloud.api.feign.system.errorlog.ErrorLogServiceFeign;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * controller日志
 *
 * @author cui_feng
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ControllerLogAspect {

	private final ObjectMapper objectMapper;
	private final ErrorLogServiceFeign errorLogService;
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	public ControllerLogAspect(ObjectMapper objectMapper,ErrorLogServiceFeign errorLogService) {
		this.objectMapper = objectMapper;
		this.errorLogService = errorLogService;
	}

	/**
	 * AOP 环切 控制器 记录controller异常
	 *
	 * @param point JoinPoint
	 * @return Object
	 * @throws Throwable 异常
	 */
	@Around(
		"(execution(!static * *(..)) && " +
			"(@within(org.springframework.stereotype.Controller) || " +
			"@within(org.springframework.web.bind.annotation.RestController))) " +
			"|| execution(!static * com.matrix.cola.cloud.common.controller..*.*(..)))"
	)
	public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		// 请求参数处理
		final Map<String, Object> paraMap = new HashMap<>(16);
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = getMethodParameter(method, i);
			// PathVariable 参数跳过
			PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
			if (pathVariable != null) {
				continue;
			}
			RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
			String parameterName = methodParam.getParameterName();
			Object value = args[i];

			if (requestBody != null) {
				if (value == null) {
					paraMap.put(parameterName, null);
				} else if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
					paraMap.put(parameterName, value);
				} else {
					paraMap.putAll(BeanUtil.beanToMap(value));
				}
				continue;
			}

			if (value instanceof HttpServletRequest) {
				paraMap.putAll(((HttpServletRequest) value).getParameterMap());
				continue;
			} else if (value instanceof WebRequest) {
				paraMap.putAll(((WebRequest) value).getParameterMap());
				continue;
			} else if (value instanceof HttpServletResponse) {
				continue;
			} else if (value instanceof MultipartFile) {
				MultipartFile multipartFile = (MultipartFile) value;
				String name = multipartFile.getName();
				String fileName = multipartFile.getOriginalFilename();
				paraMap.put(name, fileName);
				continue;
			}

			// 请求参数
			RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
			String paraName = parameterName;

			if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
				paraName = requestParam.value();
			}

			if (value == null) {
				paraMap.put(paraName, null);
			} else if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
				paraMap.put(paraName, value);
			} else if (value instanceof InputStream) {
				paraMap.put(paraName, "InputStream");
			} else if (value instanceof InputStreamSource) {
				paraMap.put(paraName, "InputStreamSource");
			} else if (canJsonSerialize(value)) {
				paraMap.put(paraName, value);
			} else {
				paraMap.put(paraName, "【注意】不能序列化为json");
			}
		}

		HttpServletRequest request = WebUtil.getRequest();
		String requestURI = Objects.requireNonNull(request).getRequestURI();
		String requestMethod = request.getMethod();

		StringBuilder beforeReqLog = new StringBuilder(300);
		List<Object> beforeReqArgs = new ArrayList<>();

		beforeReqLog.append("\n\n================  请求开始  ================\n");
		beforeReqLog.append("请求地址： {}: {}\n");
		beforeReqArgs.add(requestMethod);
		beforeReqArgs.add(requestURI);

		if (paraMap.isEmpty()) {
			beforeReqLog.append("请求参数: 无\n");
		} else {
			beforeReqLog.append("请求参数: {}\n");
			beforeReqArgs.add(JSONUtil.toJsonStr(paraMap));
		}

		// 请求头
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			String headerValue = request.getHeader(headerName);
			beforeReqLog.append("请求头：  {} : {}\n");
			beforeReqArgs.add(headerName);
			beforeReqArgs.add(headerValue);
		}

		// 执行时间
		long startNs = System.nanoTime();
		log.info(beforeReqLog.toString(), beforeReqArgs.toArray());
		// 执行后的日志
		StringBuilder afterReqLog = new StringBuilder(200);
		List<Object> afterReqArgs = new ArrayList<>();

		try {
			// 执行结果
			Object result = point.proceed();

			afterReqLog.append("\n\n返回结果：");
			afterReqLog.append("\n{}");
			afterReqArgs.add(JSONUtil.toJsonStr(result));

			return result;
		} catch (Exception error) {
			try{
				// 添加错误日志
				ErrorLogEntity errorLog = new ErrorLogEntity();
				errorLog.setIp(WebUtil.getIP());
				errorLog.setMethod(requestMethod);
				String url = requestURI;
				if (url.length()>200) {
					url = url.substring(url.length() -200);
				}
				errorLog.setUrl(url);
				String token = WebUtil.getToken();
				if (ObjectUtil.isNotNull(token) && token.length()>400) {
					token = token.substring(0,400);
				}
				errorLog.setToken(token);
				if (paraMap.isEmpty()) {
					errorLog.setParam("无");
				} else {
					String param = JSONUtil.toJsonStr(paraMap);
					if (param.length()>1000) {
						param = param.substring(0,1000);
					}
					errorLog.setParam(param);
				}
				String title = error.getMessage() == null ? error.toString() : error.getMessage();
				if (ObjectUtil.isNotNull(title) && title.length()>1000) {
					title = title.substring(0,1000);
				}
				errorLog.setTitle(title);

				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				error.printStackTrace(printWriter);
				String errMsg = stringWriter.toString();
				if (errMsg.length() > 8000) {
					errMsg = errMsg.substring(0,8000);
				}
				errorLog.setMessage(errMsg);
				errorLogService.add(errorLog);
			} catch (Exception e) {
				log.trace(e.getMessage());
			}


			// 打印错误日志
			StringBuilder errReqLog = new StringBuilder(200);
			List<Object> errReqArgs = new ArrayList<>();

			errReqLog.append("\n\n↓↓↓↓↓↓↓↓↓↓异常开始↓↓↓↓↓↓↓↓↓↓\n");
			errReqLog.append("地址：{}:{}\n");
			errReqArgs.add(requestMethod);
			errReqArgs.add(requestURI);
			if (paraMap.isEmpty()) {
				errReqLog.append("参数: 无\n");
			} else {
				errReqLog.append("参数: {}\n");
				errReqArgs.add(JSONUtil.toJsonStr(paraMap));
			}
			errReqLog.append("异常描述：\n");
			errReqLog.append("{}\n");
			errReqArgs.add(error.getMessage());
			errReqLog.append("详细信息：\n");
			errReqLog.append("{}");
			errReqArgs.add(JSONUtil.toJsonStr(error));
			errReqLog.append("\n↑↑↑↑↑↑↑↑↑↑异常结束↑↑↑↑↑↑↑↑↑↑\n");
			log.error(errReqLog.toString(), errReqArgs.toArray());
			// 抛出异常供后续处理
			throw error;
		} finally {
			long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
			afterReqLog.append("\n响应地址： {}: {}\n");
			afterReqArgs.add(requestMethod);
			afterReqArgs.add(requestURI);
			afterReqLog.append("执行时间： {} ms\n");
			afterReqArgs.add(tookMs);
			afterReqLog.append("================  请求结束   ================\n");
			log.info(afterReqLog.toString(), afterReqArgs.toArray());
		}
	}

	private boolean canJsonSerialize(Object value) {
		try {
			objectMapper.writeValueAsBytes(value);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public MethodParameter getMethodParameter(Method constructor, int parameterIndex) {
		MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
		methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
		return methodParameter;
	}
}
