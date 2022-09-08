package com.matrix.cola.cloud.service.system.menu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;
import com.matrix.cola.cloud.api.entity.system.menu.MenuTree;
import com.matrix.cola.cloud.api.entity.system.role.RoleMenuEntity;
import com.matrix.cola.cloud.common.service.AbstractEntityService;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeService;
import com.matrix.cola.cloud.service.system.menu.mapper.MenuMapper;
import com.matrix.cola.cloud.service.system.menu.service.MenuService;
import com.matrix.cola.cloud.service.system.role.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 菜单实体类服务接口实现类
 * @author : cui_feng
 * @since : 2022-05-11 17:11
 */
@Service
public class MenuServiceImpl extends AbstractEntityService<MenuEntity, MenuMapper> implements MenuService {

    @Autowired
    DataScopeService dataScopeService;

    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public List<MenuEntity> getMenuByRoleIds(List<Long> roleIdList) {
        // 超级管理员
        if (roleIdList.contains(ColaConstant.ADMINISTRATOR_ID)) {
            return getMapper().getAllMenus();
        }
        return getMapper().getMenuByRoleIds(roleIdList);
    }

    @Override
    public List<MenuEntity> getButtonsByRoleIds(List<Long> roleIdList) {
        // 超级管理员
        if (roleIdList.contains(ColaConstant.ADMINISTRATOR_ID)) {
            return getMapper().getAllButtons();
        }
        return getMapper().getButtonsByRoleIds(roleIdList);
    }

    @Override
    public Result getMenuTree(Query<MenuEntity> query) {

        if (ObjectUtil.isNotNull(query)) {
            query.orderBy("orders");
        }
        return Result.list(MenuTree.getMenuTree(getList(query)));
    }

    @Override
    public Result deleteMenu(MenuEntity menuEntity) {
        if (ObjectUtil.isNull(menuEntity) || ObjectUtil.isNull(menuEntity.getId())) {
            return Result.err("删除失败，数据不能为空");
        }

        LambdaQueryWrapper<MenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuEntity::getParentId,menuEntity.getId());
        if (getCount(queryWrapper) > 0) {
            return Result.err("删除失败，该菜单下还有子菜单，不能删除");
        }

        LambdaQueryWrapper<DataScopeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataScopeEntity::getMenuId, menuEntity.getId());
        if (dataScopeService.getCount(wrapper) > 0) {
            return Result.err("删除失败，该菜单下还有关联的数据权限数据，不能删除");
        }

        LambdaQueryWrapper<RoleMenuEntity> query = new LambdaQueryWrapper<>();
        query.eq(RoleMenuEntity::getMenuId, menuEntity.getId());
        if (roleMenuService.getCount(query) > 0) {
            return Result.err("删除失败，该菜单下还有关联的角色，不能删除");
        }

        if (getMapper().deleteMenu(menuEntity.getId()) == ColaConstant.YES) {
            return Result.ok();
        } else {
            rollback();
        }
        return Result.err("删除失败");
    }

    @Override
    protected Result validate(MenuEntity menu) {
        if (StrUtil.isEmpty(menu.getName())) {
            return Result.err("操作失败，菜单名称不能为空");
        }
        if (StrUtil.isEmpty(menu.getCode())) {
            return Result.err("操作失败，菜单编码不能为空");
        }
        if (StrUtil.isEmpty(menu.getUrl())) {
            return Result.err("操作失败，菜单URL不能为空");
        }
        if (StrUtil.isEmpty(menu.getComponent())) {
            return Result.err("操作失败，菜单组件不能为空");
        }

        LambdaQueryWrapper<MenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuEntity::getCode,menu.getCode().trim());
        MenuEntity menuEntity = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(menuEntity) && !Objects.equals(menu.getId(),menuEntity.getId())) {
            return Result.err("操作失败，菜单编码已存在");
        }

        queryWrapper.clear();
        queryWrapper.eq(MenuEntity::getName, menu.getName().trim());
        menuEntity = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(menuEntity) && !Objects.equals(menu.getId(),menuEntity.getId())) {
            return Result.err("操作失败，菜单名称已存在");
        }

        queryWrapper.clear();
        queryWrapper.eq(MenuEntity::getId, menu.getParentId());
        if (!ObjectUtil.equal(ColaConstant.TREE_ROOT_ID, menu.getParentId()) && ObjectUtil.isNull(getOne(queryWrapper))) {
            return Result.err("操作失败，没有找到父节点");
        }

        return Result.ok();
    }

}
