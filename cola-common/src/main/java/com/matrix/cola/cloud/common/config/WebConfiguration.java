
package com.matrix.cola.cloud.common.config;


import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.matrix.cola.cloud.common.error.GlobalErrorAttributes;
import com.matrix.cola.cloud.common.error.GlobalErrorController;
import com.matrix.cola.cloud.common.interceptor.Oauth2TokenFeignClientInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 统一异常处理
 *
 * @author cui_feng
 * @since 2022-04-22 21:18
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ComponentScan("com.matrix.cola.cloud.api")
@EnableFeignClients("com.matrix.cola.cloud.api")
public class WebConfiguration {

	private final ServerProperties serverProperties;

	@Bean
	@ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
	public DefaultErrorAttributes errorAttributes() {
		return new GlobalErrorAttributes();
	}

	@Bean
	@ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
	public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
		return new GlobalErrorController(errorAttributes, serverProperties.getError());
	}

	@Bean
	public RequestInterceptor Oauth2TokenFeignClientInterceptor () {
		return new Oauth2TokenFeignClientInterceptor();
	}

	@Bean
	Logger.Level feignLogLevel() {
		return Logger.Level.FULL;
	}

	/**
	 * 配置Json解析时区和日期时间格式
	 * @return 自定义JacksonObjectMapperBuilder
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {

		final String dateFormat = "yyyy-MM-dd";
		final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat);

		return builder -> {
			builder.dateFormat(format);
			builder.simpleDateFormat(dateTimeFormat);
			builder.timeZone(TimeZone.getDefault());
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
			builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
		};
	}
}
