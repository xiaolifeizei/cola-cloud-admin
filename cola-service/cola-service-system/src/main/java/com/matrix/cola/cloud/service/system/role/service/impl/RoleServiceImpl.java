package com.matrix.cola.cloud.service.system.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Objects;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.common.service.AbstractColaEntityService;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeService;
import com.matrix.cola.cloud.service.system.role.mapper.RoleMapper;
import com.matrix.cola.cloud.service.system.role.service.RoleMenuService;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import com.matrix.cola.cloud.service.system.role.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色实体服务实现类
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:47
 */
@Service
public class RoleServiceImpl extends AbstractColaEntityService<RoleEntity, RoleMapper> implements RoleService {

    @Autowired
    @Lazy
    RoleUserService roleUserService;

    @Autowired
    @Lazy
    RoleMenuService roleMenuService;

    @Autowired
    DataScopeService dataScopeService;

    @Override
    protected Result validate(RoleEntity po) {
        if (ObjectUtil.isEmpty(po.getName())) {
            return Result.err("操作失败，角色名称不能为空");
        }
        if (ObjectUtil.isEmpty(po.getCode())) {
            return Result.err("操作失败，角色编码不能为空");
        }

        if (!Objects.equal(ColaConstant.TREE_ROOT_ID, po.getParentId()) && ObjectUtil.isNull(getOne(po.getParentId()))) {
            return Result.err("操作失败，没有找到父节点");
        }

        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getName, po.getName()).or().eq(RoleEntity::getCode,po.getCode());
        RoleEntity rolePO = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(rolePO) && !Objects.equal(po.getId(),rolePO.getId())) {
            return Result.err("操作失败，角色名或角色编码已存在");
        }

        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteRole(RoleEntity rolePO) {
        if (ObjectUtil.isNull(rolePO) || ObjectUtil.isNull(rolePO.getId())) {
            return Result.err("删除失败，id不能为空");
        }

        LambdaQueryWrapper<DataScopeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataScopeEntity::getRoleIds,rolePO.getId().toString())
                .likeRight(DataScopeEntity::getRoleIds,rolePO.getId().toString())
                .likeLeft(DataScopeEntity::getRoleIds,rolePO.getId().toString())
                .like(DataScopeEntity::getRoleIds,rolePO.getId().toString());

        if (dataScopeService.getCount(queryWrapper) > 0) {
            return Result.err("删除失败，该角色还关联有数据权限数据，不能删除");
        }

        // 删除角色时同时删除角色用户和角色菜单中的数据
        if (getMapper().deleteRole(rolePO.getId()) == ColaConstant.YES &&
                roleUserService.deleteRoleUsersByRoleId(rolePO.getId()).isSuccess() &&
                roleMenuService.deleteRoleMenusByRoleId(rolePO.getId()).isSuccess()) {
            return Result.ok();
        } else {
            rollback();
        }

        return Result.err("删除失败");
    }
}
