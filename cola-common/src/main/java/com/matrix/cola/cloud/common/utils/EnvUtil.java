package com.matrix.cola.cloud.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.env.Environment;

/**
 * 环境变量读取工具类
 *
 * @author : cui_feng
 * @since : 2022-09-02 16:04
 */
public class EnvUtil {

    /**
     * 获取环境变量的值
     * @param key 环境变量名
     * @return 值
     */
    public static String getEnvValue(String key) {
        if (StrUtil.isEmpty(key)) {
            return "";
        }
        Environment env = SpringUtil.getApplicationContext().getBean(Environment.class);
        return env.getProperty(key);
    }

    /**
     * 获取环境变量的值，如果null或空值则返回默认值
     * @param key 环境变量名
     * @param defaultStr 默认值
     * @return
     */
    public static String getEnvValue(String key,String defaultStr) {
        return StrUtil.emptyToDefault(getEnvValue(key),defaultStr);
    }
}
