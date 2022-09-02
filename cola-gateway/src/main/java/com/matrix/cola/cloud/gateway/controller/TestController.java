package com.matrix.cola.cloud.gateway.controller;

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

    @GetMapping("/test2")
    public String test2() {
        return "Test2";
    }
}
