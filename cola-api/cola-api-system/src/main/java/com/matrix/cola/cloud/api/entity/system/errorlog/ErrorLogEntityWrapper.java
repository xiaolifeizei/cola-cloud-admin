package com.matrix.cola.cloud.api.entity.system.errorlog;

import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;
import lombok.Data;

/**
 * 系统错误日志
 *
 * @author : cui_feng
 * @since : 2022-06-10 12:39
 */
@Data
public class ErrorLogEntityWrapper extends BaseColaEntityWrapper {

    /**
     * ip地址
     */
    private String ip;
    /**
     * 用户token
     */
    private String token;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 错误标题
     */
    private String title;
    /**
     * 错误详细信息
     */
    private String message;
}
