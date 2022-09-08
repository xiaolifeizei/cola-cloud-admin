package com.matrix.cola.cloud.service.system.role.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.api.entity.system.role.GrantMenu;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleMenuEntity;

/**
 * 角色菜单关联实体类接口
 *
 * @author : cui_feng
 * @since : 2022-05-11 17:20
 */
public interface RoleMenuService extends BaseEntityService<RoleMenuEntity> {

    /**
     * 删除角色菜单，物理删除
     * @param roleMenuPO 角色菜单对象，必须包含id
     * @return 返回统一类型
     */
    Result deleteRoleMenu(RoleMenuEntity roleMenuPO);

    /**
     * 通过角色id删除角色菜单，物理删除
     * @param roleId 角色id
     * @return 返回通用类型
     */
    Result deleteRoleMenusByRoleId(Long roleId);

    /**
     * 通过角色获取已经分配的菜单
     * @param rolePO 角色对象，必须要有id
     * @return 返回通用类型
     */
    Result getRoleMenusByRoleId(RoleEntity rolePO);

    /**
     * 给角色分配菜单
     * @param grantMenu 给角色分配菜单的包装类
     * @return 返回通用类型
     */
    Result grantMenus(GrantMenu grantMenu);
}
