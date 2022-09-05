package com.matrix.cola.cloud.service.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cui_feng
 * @since : 2022-09-02 14:23
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public String test() {
        int a = 1 / 0;
        return "TTTTTT";
    }
}
