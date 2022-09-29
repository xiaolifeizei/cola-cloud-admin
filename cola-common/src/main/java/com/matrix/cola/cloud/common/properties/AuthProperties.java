package com.matrix.cola.cloud.common.properties;

import lombok.Data;
import lombok.Getter;
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

    @Getter
    private static List<String> defaultSkipUrl = new ArrayList<>();

    static {
        defaultSkipUrl.add("/oauth/token/**");
        defaultSkipUrl.add("/favicon.ico");
    }

    /**
     * 放行API集合
     */
    private List<String> skipUrl = new ArrayList<>();


}
