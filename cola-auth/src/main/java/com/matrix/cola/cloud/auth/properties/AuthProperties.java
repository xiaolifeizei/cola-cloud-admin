package com.matrix.cola.cloud.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行权限配置
 *
 * @author : cui_feng
 * @since : 2022-09-27 10:09
 */
@Data
@RefreshScope
@ConfigurationProperties("cola.security")
public class AuthProperties {

    /**
     * 放行API集合
     */
    private List<String> skipUrl = new ArrayList<>();

}
