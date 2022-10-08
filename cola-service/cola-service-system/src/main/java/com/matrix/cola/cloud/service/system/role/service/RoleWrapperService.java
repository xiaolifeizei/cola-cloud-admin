package com.matrix.cola.cloud.service.system.role.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityWrapperService;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntityWrapper;

/**
 * 角色包装类接口
 *
 * @author : cui_feng
 * @since : 2022-05-31 15:56
 */
public interface RoleWrapperService extends BaseColaEntityWrapperService<RoleEntity, RoleEntityWrapper> {

    Result getRoleTree(Query<RoleEntity> query);
}
