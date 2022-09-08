package com.matrix.cola.cloud.service.system.user.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.entity.system.user.GrantRole;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntityWrapper;

/**
 * 用户实体类服务接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface UserService extends BaseColaEntityService<UserEntity> {

    /**
     * 给用户分配角色
     * @param grantRole 分配对象
     * @return 返回统一类型
     */
    Result grantRoles(GrantRole grantRole);

    /**
     * 个人中心修改用户信息，包括修改密码
     * @param user 用户对象
     * @return 返回统一类型
     */
    Result updateUserInfo(UserEntityWrapper user);

    /**
     * 重置密码
     * @param userPO 用户对象
     * @return 返回统一类型
     */
    Result resetPassword(UserEntity userPO);
}
