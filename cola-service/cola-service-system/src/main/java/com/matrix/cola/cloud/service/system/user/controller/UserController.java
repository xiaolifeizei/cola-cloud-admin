package com.matrix.cola.cloud.service.system.user.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.user.GrantRole;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.role.service.RoleUserService;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import com.matrix.cola.cloud.service.system.user.service.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    RoleUserService roleUserService;

    public UserController(UserService service, UserWrapper wrapperService) {
        super(service, wrapperService);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result getPage(Query<UserEntity> query) {
        return super.getPage(query);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result add(UserEntity entity) {
        return super.add(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result update(UserEntity entity) {
        return super.update(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result delete(UserEntity entity) {
        return super.delete(entity);
    }

    @PostMapping("/getRoleUsersByUser")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result getRoleUsersByUser(@RequestBody UserEntity userPO) {
        return roleUserService.getRoleUsersByUser(userPO);
    }

    @PostMapping("/grantRoles")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result grantRoles(@RequestBody GrantRole grantRole) {
        return service.grantRoles(grantRole);
    }
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserEntityWrapper user) {
        return service.updateUserInfo(user);
    }

    @PostMapping("/resetPassword")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result resetPassword(@RequestBody UserEntity userPO) {
        return service.resetPassword(userPO);
    }
}
