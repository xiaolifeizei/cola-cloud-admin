package com.matrix.cola.cloud.service.system.menu.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;
import com.matrix.cola.cloud.common.controller.AbstractEntityController;
import com.matrix.cola.cloud.service.system.menu.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 菜单controller
 *
 * @author : cui_feng
 * @since : 2022-05-11 14:16
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends AbstractEntityController<MenuEntity, MenuService> {

    public MenuController(MenuService service) {
        super(service);
    }

    @PostMapping("/getMenuTree")
    public Result getMenuTree(@RequestBody Query<MenuEntity> query) {
        return service.getMenuTree(query);
    }

    @PreAuthorize("hasAuthority('administrator')")
    public Result add(@RequestBody MenuEntity menuPO) {
        return super.add(menuPO);
    }

    @PreAuthorize("hasAuthority('administrator')")
    public Result update(@RequestBody MenuEntity menuPO) {
        return super.update(menuPO);
    }

    @PreAuthorize("hasAuthority('administrator')")
    public Result delete(@RequestBody MenuEntity menuPO) {
        return service.deleteMenu(menuPO);
    }
}
