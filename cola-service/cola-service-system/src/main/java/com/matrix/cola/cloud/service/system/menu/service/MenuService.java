package com.matrix.cola.cloud.service.system.menu.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;

import java.util.List;

/**
 * 菜单实体类接口
 *
 * @author : cui_feng
 * @since : 2022-05-11 14:28
 */
public interface MenuService extends BaseEntityService<MenuEntity> {

    /**
     * 通过分配给用户的角色获取菜单
     * @param roleIdList 角色id列表
     * @return 菜单列表
     */
    List<MenuEntity> getMenuByRoleIds(List<Long> roleIdList);

    /**
     * 通过分配给用户的角色获取按钮
     * @param roleIdList 角色id列表
     * @return 按钮列表
     */
    List<MenuEntity> getButtonsByRoleIds(List<Long> roleIdList);

    /**
     * 通过查询条件获取菜单树
     * @param query 查询条件
     * @return 通用返回类型
     */
    Result getMenuTree(Query<MenuEntity> query);

    /**
     * 通过id号删除菜单，此处为物理删除
     * @param menuPO 菜单对象，必须包含id
     * @return 统一结果类型
     */
    Result deleteMenu(MenuEntity menuPO);
}
