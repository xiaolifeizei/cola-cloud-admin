package com.matrix.cola.cloud.service.system.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.role.RoleUserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.service.AbstractEntityService;
import com.matrix.cola.cloud.service.system.role.mapper.RoleUserMapper;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import com.matrix.cola.cloud.service.system.role.service.RoleUserService;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

;

/**
 * 角色与用户映射实体服务接口实现类
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:52
 */
@Service
public class RoleUserServiceImpl extends AbstractEntityService<RoleUserEntity, RoleUserMapper> implements RoleUserService {

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    RoleService roleService;

    @Override
    public List<RoleUserEntity> getRoleUsersByUser(Long userid) {
        if (ObjectUtil.isNull(userid)) {
            return null;
        }
        LambdaQueryWrapper<RoleUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUserEntity::getUserId,userid);
        return super.getList(queryWrapper);
    }

    @Override
    public Result getRoleUsersByUser(UserEntity userEntity) {
        if (ObjectUtil.isNull(userEntity) || ObjectUtil.isNull(userEntity.getId())) {
            return Result.err("查询角色用户信息失败，用户不能为空");
        }
        return Result.list(getRoleUsersByUser(userEntity.getId()));
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userid) {
        if (ObjectUtil.isNull(userid)) {
            return null;
        }
        List<RoleUserEntity> roleUserList = this.getRoleUsersByUser(userid);
        if (ObjectUtil.isEmpty(roleUserList)) {
            return null;
        }
        List<Long> roleIdList = new ArrayList<>();
        for (RoleUserEntity roleUserEntity : roleUserList) {
            roleIdList.add(roleUserEntity.getRoleId());
        }
        return roleIdList;
    }

    @Override
    public Result deleteRoleUser(RoleUserEntity roleUserEntity) {
        if (ObjectUtil.isNull(roleUserEntity) || ObjectUtil.isNull(roleUserEntity.getId())) {
            return Result.err("删除失败，id不能为空");
        }
        if (getMapper().deleteRoleUser(roleUserEntity.getId()) == ColaConstant.YES) {
            return Result.ok();
        }
        return Result.err("删除失败");
    }

    @Override
    public Result deleteRoleUsersByRoleId(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return Result.err("删除失败，角色id不能为空");
        }
        getMapper().deleteRoleUsersByRoleId(roleId);
        return Result.ok();
    }

    @Override
    public Result deleteRoleUsersByUserId(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Result.err("删除失败，用户id不能为空");
        }
        getMapper().deleteRoleUsersByUserId(userId);
        return Result.ok();
    }

    @Override
    protected Result validate(RoleUserEntity entity) {
        if (ObjectUtil.isNull(entity.getUserId())) {
            return Result.err("操作失败，用户id不能为空");
        }
        if (ObjectUtil.isNull(entity.getRoleId())) {
            return Result.err("操作失败，角色id不能为空");
        }
        if (ObjectUtil.isNull(userService.getOne(entity.getUserId()))) {
            return Result.err("操作失败，没有找到用户 { userid: " + entity.getUserId() + ", roleid: " + entity.getRoleId() + "}");
        }
        if (ObjectUtil.isNull(roleService.getOne(entity.getRoleId()))) {
            return Result.err("操作失败，没有找到角色 { userid: " + entity.getUserId() + ", roleid: " + entity.getRoleId() + "}");
        }
        return Result.ok();
    }
}
