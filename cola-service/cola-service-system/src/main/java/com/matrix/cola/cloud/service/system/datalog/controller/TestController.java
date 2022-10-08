package com.matrix.cola.cloud.service.system.datalog.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.feign.system.datalog.DataLogServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test
 *
 * @author : cui_feng
 * @since : 2022-10-08 16:47
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    DataLogServiceFeign dataLogServiceFeign;

    @PostMapping("/test")
    public void test() {
       Result result =  dataLogServiceFeign.addDeleteLog("system_user",new UserEntity());
        System.out.println(result.getMsg());
    }
}
