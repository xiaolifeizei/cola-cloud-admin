package com.matrix.cola.cloud.service.system.role.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.api.entity.system.role.RoleUserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;

import java.util.List;

/**
 * 角色与用户映射实体类服务接口
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:51
 */
public interface RoleUserService extends BaseEntityService<RoleUserEntity> {

    /**
     * 通过用户ID获取角色与用户映射集合
     * @param userid 用户id
     * @return 角色与用户的实体集合
     */
    List<RoleUserEntity> getRoleUsersByUser(Long userid);

    /**
     * 通过用户ID获取角色与用户映射集合
     * @param userPO 用户对象，必须要有id
     * @return 角色与用户的实体集合
     */
    Result getRoleUsersByUser(UserEntity userPO);

    /**
     * 通过用户id获取对应的角色id,如：1,2,3
     * @param userid 用户id
     * @return 该用户的角色id集合
     */
    List<Long> getRoleIdsByUserId(Long userid);

    /**
     * 删除角色用户
     * @param roleUserPO 角色用户对象，必须包含id
     * @return 返回统一类型
     */
    Result deleteRoleUser(RoleUserEntity roleUserPO);

    /**
     * 通过角色id删除角色用户
     * @param roleId 角色id
     * @return 返回统一类型
     */
    Result deleteRoleUsersByRoleId(Long roleId);

    /**
     * 通过用户id删除角色用户
     * @param userId 用户id
     * @return 返回统一类型
     */
    Result deleteRoleUsersByUserId(Long userId);
}
