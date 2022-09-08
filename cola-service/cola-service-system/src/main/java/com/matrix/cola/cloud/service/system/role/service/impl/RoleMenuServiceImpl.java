package com.matrix.cola.cloud.service.system.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;
import com.matrix.cola.cloud.api.entity.system.role.GrantMenu;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleMenuEntity;
import com.matrix.cola.cloud.common.service.AbstractEntityService;
import com.matrix.cola.cloud.service.system.menu.service.MenuService;
import com.matrix.cola.cloud.service.system.role.mapper.RoleMenuMapper;
import com.matrix.cola.cloud.service.system.role.service.RoleMenuService;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 角色菜单关联实体服务接口实现类
 *
 * @author : cui_feng
 * @since : 2022-05-11 17:23
 */
@Service
public class RoleMenuServiceImpl extends AbstractEntityService<RoleMenuEntity, RoleMenuMapper> implements RoleMenuService {

    @Autowired
    @Lazy
    RoleService roleService;

    @Autowired
    @Lazy
    MenuService menuService;

    @Override
    protected Result validate(RoleMenuEntity po) {
        if (ObjectUtil.isNull(po.getRoleId())) {
            return Result.err("操作失败，角色id不能为空");
        }
        if (ObjectUtil.isNull(po.getMenuId())) {
            return Result.err("操作失败，菜单id不能为空");
        }

        if (ObjectUtil.isNull(roleService.getOne(po.getRoleId()))) {
            return Result.err("操作失败，没有找到对应的角色 {roleid: " + po.getRoleId() + ", menuid: "+ po.getMenuId() +"}");
        }

        LambdaQueryWrapper<MenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuEntity::getId, po.getMenuId());
        if (ObjectUtil.isNull(menuService.getOne(queryWrapper))) {
            return Result.err("操作失败，没有找到对应的菜单或按钮 {roleid: " + po.getRoleId() + ", menuid: "+ po.getMenuId() +"}");
        }

        return Result.ok();
    }

    @Override
    public Result deleteRoleMenu(RoleMenuEntity roleMenuPO) {
        if (ObjectUtil.isNull(roleMenuPO) || ObjectUtil.isNull(roleMenuPO.getId())) {
            return Result.err("删除失败，id不能为空");
        }
        if (getMapper().deleteRoleMenu(roleMenuPO.getId()) == ColaConstant.YES) {
            return Result.ok("删除成功");
        }
        return Result.err("删除失败");
    }

    @Override
    public Result deleteRoleMenusByRoleId(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return Result.err("删除失败，角色id不能为空");
        }
        getMapper().deleteRoleMenusByRoleId(roleId);
        return Result.ok();
    }

    @Override
    public Result getRoleMenusByRoleId(RoleEntity rolePO) {
        if (ObjectUtil.isNull(rolePO) || ObjectUtil.isNull(rolePO.getId())) {
            return Result.ok();
        }

        LambdaQueryWrapper<RoleMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenuEntity::getRoleId, rolePO.getId());

        return Result.list(getList(queryWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result grantMenus(GrantMenu grantMenu) {
        if (ObjectUtil.isNull(grantMenu)) {
            return Result.err("分配菜单失败，参数不能为空");
        }
        if (ObjectUtil.isNull(grantMenu.getRoleId())) {
            return Result.err("分配菜单失败，角色不能为空");
        }
        if (ObjectUtil.isNull(roleService.getOne(grantMenu.getRoleId()))) {
            return Result.err("分配菜单失败，没有找到角色");
        }
        // 先删除再添加
        deleteRoleMenusByRoleId(grantMenu.getRoleId());
        if (ObjectUtil.isNotEmpty(grantMenu.getList())) {
            Result result = batchInsert(grantMenu.getList());
            if (!result.isSuccess()) {
                rollback();
                return result;
            }
        }

        return Result.ok();
    }
}
