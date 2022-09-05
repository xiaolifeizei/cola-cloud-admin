package com.matrix.cola.cloud.gateway.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xxx
 *
 * @author : cui_feng
 * @since : 2022-09-02 17:41
 */
@RestController
@RequestMapping("/test2")
public class TestController {

    @Value("${config.info}")
    private String info;

    @GetMapping("/test2")
    public String test2() {
        int a = 1 / 0;
        return StrUtil.emptyToDefault(info,"没有值");
    }
}
