package com.matrix.cola.cloud.service.system.role.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.role.GrantMenu;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.role.service.RoleMenuService;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import com.matrix.cola.cloud.service.system.role.service.RoleWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 *
 * @author : cui_feng
 * @since : 2022-05-30 13:18
 */
@RestController
@RequestMapping("/role")
public class RoleController extends AbstractColaController<RoleEntity, RoleEntityWrapper, RoleService, RoleWrapperService> {

    @Autowired
    RoleMenuService roleMenuService;

    public RoleController(RoleService service, RoleWrapperService wrapperService) {
        super(service, wrapperService);
    }

    @PostMapping("/getRoleTree")
    public Result getRoleTree(@RequestBody Query<RoleEntity> query) {
        return wrapperService.getRoleTree(query);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result delete(@RequestBody RoleEntity role) {
        return service.deleteRole(role);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result update(@RequestBody RoleEntity role) {
        return super.update(role);
    }

    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result add(@RequestBody RoleEntity role) {
        return super.add(role);
    }

    @PostMapping("/getRoleMenusByRole")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result getRoleMenusByRole(@RequestBody RoleEntity rolePO) {
        return roleMenuService.getRoleMenusByRoleId(rolePO);
    }

    @PostMapping("/grantMenus")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result grantMenus(@RequestBody GrantMenu grantMenu) {
        return roleMenuService.grantMenus(grantMenu);
    }
}
