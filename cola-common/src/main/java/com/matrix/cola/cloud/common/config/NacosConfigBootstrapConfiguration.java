package com.matrix.cola.cloud.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Nacos配置
 *
 * @author : cui_feng
 * @since : 2022-09-05 14:37
 */
@Configuration
public class NacosConfigBootstrapConfiguration {

    private final NacosConfigProperties nacosConfigProperties;

    public NacosConfigBootstrapConfiguration(NacosConfigProperties nacosConfigProperties) {
        this.nacosConfigProperties = nacosConfigProperties;
    }

    @Bean
    public void nacosConfig() {
        NacosConfigProperties.Config sharedConfig = new NacosConfigProperties.Config();
        sharedConfig.setGroup("DEFAULT_GROUP");
        sharedConfig.setDataId("cola-admin.yaml");
        List<NacosConfigProperties.Config> sharedConfigs =  nacosConfigProperties.getSharedConfigs();
        if (ObjectUtil.isEmpty(sharedConfigs)) {
            sharedConfigs = new ArrayList<>();
        }
        sharedConfig.setRefresh(true);
        sharedConfigs.add(sharedConfig);
        nacosConfigProperties.setSharedConfigs(sharedConfigs);
    }
}
