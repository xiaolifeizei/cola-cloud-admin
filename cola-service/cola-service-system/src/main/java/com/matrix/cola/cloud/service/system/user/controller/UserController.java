package com.matrix.cola.cloud.service.system.user.controller;

import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import com.matrix.cola.cloud.service.system.user.service.UserWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 *
 * @author : cui_feng
 * @since : 2022-09-07 17:38
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractColaController<UserEntity, UserEntityWrapper, UserService, UserWrapper> {

    public UserController(UserService service, UserWrapper wrapperService) {
        super(service, wrapperService);
    }

}
