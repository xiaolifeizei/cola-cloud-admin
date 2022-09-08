package com.matrix.cola.cloud.service.system.user.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.user.GrantRole;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntityWrapper;
import com.matrix.cola.cloud.common.service.AbstractColaEntityService;
import com.matrix.cola.cloud.service.system.role.service.RoleUserService;
import com.matrix.cola.cloud.service.system.user.mapper.UserMapper;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户实体服务接口实现类
 *
 * @author : cui_feng
 * @since : 2022-04-20 13:05
 */
@Service
public class UserServiceImpl extends AbstractColaEntityService<UserEntity, UserMapper> implements UserService {

    /**
     * 默认密码
     */
    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    @Lazy
    RoleUserService roleUserService;

    @Override
    protected Result beforeAdd(UserEntity po) {
        if (StrUtil.isEmpty(po.getPassword())) {
            po.setPassword(DEFAULT_PASSWORD);
        }
        if (po.getPassword().length() <  DEFAULT_PASSWORD.length()) {
            return Result.err("添加失败，密码长度不能小于6位");
        }
        po.setPassword(SecureUtil.sha1(SecureUtil.md5(po.getPassword())));
        return Result.ok();
    }

    @Override
    protected Result validate(UserEntity po) {
        if (StrUtil.isEmpty(po.getName())) {
            return Result.err("操作失败，姓名不能为空");
        }
        if (StrUtil.isEmpty(po.getLoginName())) {
            return Result.err("操作失败，登陆名不能为空");
        }
        if (StrUtil.isEmpty(po.getGroupId())) {
            return Result.err("操作失败，所属机构不能为空");
        }
        if (StrUtil.isNotEmpty(po.getPhone()) && !Validator.isMobile(po.getPhone())) {
            return Result.err("操作失败，用户手机号码格式不正确");
        }
        if (StrUtil.isNotEmpty(po.getIds()) && !Validator.isCitizenId(po.getIds())) {
            return Result.err("操作失败，身份证号码格式不正确");
        }
        return super.validate(po);
    }

    @Override
    protected Result beforeUpdate(UserEntity po) {
        cacheProxy.evict(ColaCacheName.USER_NAME,po.getId().toString());
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result grantRoles(GrantRole grantRole) {
        if (ObjectUtil.isNull(grantRole)) {
            return Result.err("分配角色失败，参数不能为空");
        }
        if (ObjectUtil.isNull(grantRole.getUserId())) {
            return Result.err("分配角色失败，用户id不能为空");
        }
        roleUserService.deleteRoleUsersByUserId(grantRole.getUserId());
        if (ObjectUtil.isNotEmpty(grantRole.getList())) {
            Result result = roleUserService.batchInsert(grantRole.getList());
            if (!result.isSuccess()) {
                rollback();
                return result;
            }
        }
        cacheProxy.evict(ColaCacheName.USER_ROLE_IDS,grantRole.getUserId().toString());
        cacheProxy.evict(ColaCacheName.USER_ROLE_CODES,grantRole.getUserId().toString());
        return Result.ok();
    }

    @Override
    public Result updateUserInfo(UserEntityWrapper user) {
        if (ObjectUtil.isEmpty(user)) {
            return Result.err("修改失败，参数不能为空");
        }

        if (ObjectUtil.isNull(user.getId())) {
            return Result.err("修改失败，用户id不能为空");
        }

        UserEntity userPO = getOne(user.getId());
        if (ObjectUtil.isNull(userPO)) {
            return Result.err("修改失败，没有找到用户");
        }

        if (StrUtil.isNotEmpty(user.getName())) {
            userPO.setName(user.getName());
        }

        if (StrUtil.isNotEmpty(user.getPhone())) {
            userPO.setPhone(user.getPhone());
        }

        if (StrUtil.isNotEmpty(user.getIds())) {
            userPO.setIds(user.getIds());
        }

        if (ObjectUtil.isNotEmpty(user.getPassword())) {
            if (StrUtil.isEmpty(user.getNewPassword())) {
                return Result.err("修改失败，新密码不能为空");
            }
            if (user.getNewPassword().length() < DEFAULT_PASSWORD.length() ) {
                return Result.err("修改失败，新密码长度不能小于6位");
            }
            if (!SecureUtil.sha1(SecureUtil.md5(user.getPassword())).equals(userPO.getPassword())) {
                return Result.err("修改失败，原密码不正确");
            }
            userPO.setPassword(SecureUtil.sha1(SecureUtil.md5(user.getNewPassword())));
        }

        return modify(userPO);
    }

    @Override
    public Result resetPassword(UserEntity userPO) {
        if (ObjectUtil.isNull(userPO) || ObjectUtil.isNull(userPO.getId())) {
            return Result.err("重置密码失败，用户不能为空");
        }

        UserEntity user = getOne(userPO);
        if (ObjectUtil.isNull(user)) {
            return Result.err("重置密码失败，没有找到用户");
        }
        user.setPassword(SecureUtil.sha1(SecureUtil.md5(DEFAULT_PASSWORD)));
        return modify(user);
    }
}
