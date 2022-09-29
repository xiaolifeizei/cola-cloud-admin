package com.matrix.cola.cloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.cola.cloud.common.properties.AuthProperties;
import com.matrix.cola.cloud.common.utils.JwtTokenUtil;
import com.matrix.cola.cloud.gateway.util.ResponseOut;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 鉴权认证
 *
 * @author Chill
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

	private static final String URL_ALL_TARGET = "/**";

	private AuthProperties authProperties;

	private ObjectMapper objectMapper;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		exchange.getAttributes().put("isSkip",Boolean.FALSE);
		if (isSkip(path)) {
			exchange.getAttributes().put("isSkip",Boolean.TRUE);
			return chain.filter(exchange);
		}
		ServerHttpResponse resp = exchange.getResponse();
		String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (StrUtil.isEmpty(token)) {
			return unAuth(resp, "缺失token,鉴权失败");
		}

		if (!JwtTokenUtil.isBearer(token)) {
			return unAuth(resp, "token类型不匹配");
		}

		return chain.filter(exchange);
	}

	private boolean isSkip(String path) {
		return AuthProperties.getDefaultSkipUrl().stream().map(url -> url.replace(URL_ALL_TARGET, "")).anyMatch(path::startsWith)
			|| authProperties.getSkipUrl().stream().map(url -> url.replace(URL_ALL_TARGET, "")).anyMatch(path::startsWith);
	}

	private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
		resp.setStatusCode(HttpStatus.UNAUTHORIZED);
		resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		String result = "";
		try {
			result = objectMapper.writeValueAsString(ResponseOut.out(msg));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
		return resp.writeWith(Flux.just(buffer));
	}


	@Override
	public int getOrder() {
		return -1000;
	}

}
