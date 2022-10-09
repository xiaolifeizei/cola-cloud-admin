
package com.matrix.cola.cloud.common.error;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.cola.cloud.api.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {


	/**
	 * ModelAndView 异常处理
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler the executed handler, or {@code null} if none chosen at the
	 * time of the exception (for example, if multipart resolution failed)
	 * @param ex the exception that got thrown during handler execution
	 * @return null
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) {
		ex.printStackTrace();
		Map<String, Object> body = BeanUtil.beanToMap(Result.err("哎呀！系统出错了"));
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setObjectMapper(new ObjectMapper());
		view.setContentType(MediaType.APPLICATION_JSON_VALUE);
		return new ModelAndView(view, body);
	}
}
