package com.matrix.cola.cloud.service.system.role.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;


/**
 * 角色实体类服务接口
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:43
 */
public interface RoleService extends BaseColaEntityService<RoleEntity> {

    /**
     * 删除角色（物理删除）
     * @param roleEntity 角色对象，必须包含id
     * @return 统一返回
     */
    Result deleteRole(RoleEntity roleEntity);
}
