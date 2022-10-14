package com.matrix.cola.cloud.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据权限配置
 *
 * @author : cui_feng
 * @since : 2022-10-14 18:06
 */
@Data
@RefreshScope
@ConfigurationProperties("cola.data-scope")
public class DataScopeProperties {

    private boolean enabled = true;

    /**
     * 是否开启默认的数据权限，默认不开启，默认的数据权限类型为本机构及下级机构
     */
    private boolean enableDefault = false;

    /**
     * 忽略数据权限的表
     */
    private List<String> ignoreTables = new ArrayList<>();


    private List<String> defaultIgnoreTables = Arrays.asList(
            "system_dict",
            "system_err_log",
            "system_data_log",
            "system_group",
            "system_role_user",
            "system_role_menu",
            "system_data_scope");
}
