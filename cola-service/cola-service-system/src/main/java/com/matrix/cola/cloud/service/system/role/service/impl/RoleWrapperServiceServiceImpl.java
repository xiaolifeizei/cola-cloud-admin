package com.matrix.cola.cloud.service.system.role.service.impl;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntityWrapper;
import com.matrix.cola.cloud.api.entity.system.role.RoleTree;
import com.matrix.cola.cloud.common.service.AbstractColaEntityWrapperService;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import com.matrix.cola.cloud.service.system.role.service.RoleWrapperService;
import org.springframework.stereotype.Service;

/**
 * 角色包装类接口实现类
 *
 * @author : cui_feng
 * @since : 2022-05-31 15:55
 */
@Service
public class RoleWrapperServiceServiceImpl extends AbstractColaEntityWrapperService<RoleEntity, RoleEntityWrapper, RoleService> implements RoleWrapperService {

    @Override
    public Result getRoleTree(Query<RoleEntity> query) {
        return Result.list(RoleTree.getRoleTree(getWrapperList(query)));
    }
}
