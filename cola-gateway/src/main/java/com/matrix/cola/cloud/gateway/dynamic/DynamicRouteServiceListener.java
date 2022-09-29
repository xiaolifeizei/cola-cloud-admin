package com.matrix.cola.cloud.gateway.dynamic;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 动态路由监听器
 *
 * @author Chill
 */
@Order
@Slf4j
@Component
public class DynamicRouteServiceListener {

	@Value("${spring.application.name}")
	private String appName;
	private final DynamicRouteService dynamicRouteService;
	private final NacosDiscoveryProperties nacosDiscoveryProperties;
	private final NacosConfigProperties nacosConfigProperties;

	public DynamicRouteServiceListener(DynamicRouteService dynamicRouteService, NacosDiscoveryProperties nacosDiscoveryProperties, NacosConfigProperties nacosConfigProperties) {
		this.dynamicRouteService = dynamicRouteService;
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
		this.nacosConfigProperties = nacosConfigProperties;
		dynamicRouteServiceListener();
	}

	/**
	 * 监听Nacos下发的动态路由配置
	 */
	private void dynamicRouteServiceListener() {
		try {

			String dataId = appName + ".json";
			String group = nacosConfigProperties.getGroup();
			String serverAddr = nacosDiscoveryProperties.getServerAddr();
			ConfigService configService = NacosFactory.createConfigService(serverAddr);

			configService.addListener(dataId, group, new Listener() {
				@Override
				public void receiveConfigInfo(String configInfo) {
					List<RouteDefinition> routeDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);
					dynamicRouteService.updateList(routeDefinitions);
				}

				@Override
				public Executor getExecutor() {
					return null;
				}
			});
		} catch (NacosException ignored) {

		}
	}

}
