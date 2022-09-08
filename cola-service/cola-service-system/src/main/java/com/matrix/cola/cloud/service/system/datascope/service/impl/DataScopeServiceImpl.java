package com.matrix.cola.cloud.service.system.datascope.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Objects;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.common.service.AbstractColaEntityService;
import com.matrix.cola.cloud.service.system.datascope.mapper.DataScopeMapper;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据权限接口实现类
 *
 * @author : cui_feng
 * @since : 2022-06-06 11:29
 */
@Service
public class DataScopeServiceImpl extends AbstractColaEntityService<DataScopeEntity, DataScopeMapper> implements DataScopeService {

    @Override
    protected Result validate(DataScopeEntity po) {
        if (ObjectUtil.isNull(po.getMenuId())) {
            return Result.err("操作失败，关联的菜单不能为空");
        }
        if (ObjectUtil.isNull(po.getScopeType())) {
            return Result.err("操作失败，权限类型不能为空");
        }
        if (ObjectUtil.isEmpty(po.getMethod())) {
            return Result.err("操作失败，拦截的方法名不能为空");
        }

        if (ObjectUtil.isNull(po.getGlobalized()) || (po.getGlobalized() != ColaConstant.NO && po.getGlobalized() != ColaConstant.YES)) {
            return Result.err("操作失败，全局生效的值不正确，只为0或1");
        }

        if (Objects.equal(po.getGlobalized(), ColaConstant.NO) && ObjectUtil.isEmpty(po.getGroupId())) {
            return Result.err("操作失败，所属机构不能为空");
        }

        return Result.ok();
    }

    @Override
    protected Result afterAdd(DataScopeEntity po) {
        cacheProxy.put(ColaCacheName.DATA_SCOPE_LIST,ColaCacheName.DATA_SCOPE_LIST.cacheName(), getDataScopeMap());
        return super.afterAdd(po);
    }

    @Override
    protected Result afterUpdate(DataScopeEntity po) {
        cacheProxy.put(ColaCacheName.DATA_SCOPE_LIST,ColaCacheName.DATA_SCOPE_LIST.cacheName(), getDataScopeMap());
        return super.afterUpdate(po);
    }

    @Override
    public Result deleteDataScope(DataScopeEntity dataScopePO) {
        if (ObjectUtil.isNull(dataScopePO) || ObjectUtil.isNull(dataScopePO.getId())) {
            return Result.err("删除失败，参数不能为空");
        }
        if (getMapper().deleteDataScope(dataScopePO.getId()) == ColaConstant.YES) {
            HashMap<String, List<DataScopeEntity>> dataScopeCache = getDataScopeMap();
            if (ObjectUtil.isEmpty(dataScopeCache)) {
                cacheProxy.evict(ColaCacheName.DATA_SCOPE_LIST,ColaCacheName.DATA_SCOPE_LIST.cacheName());
            } else {
                cacheProxy.put(ColaCacheName.DATA_SCOPE_LIST,ColaCacheName.DATA_SCOPE_LIST.cacheName(), dataScopeCache);
            }
            return Result.ok();
        }
        return Result.err("数据权限删除失败");
    }

    @Override
    public HashMap<String, List<DataScopeEntity>> getDataScopeMap() {

        LambdaQueryWrapper<DataScopeEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<DataScopeEntity> scopeList = getList(queryWrapper);
        if (ObjectUtil.isEmpty(scopeList)) {
            return new HashMap<>();
        }
        HashMap<String, List<DataScopeEntity>> scopeMap = new HashMap<>();
        for (DataScopeEntity scopePO : scopeList) {
            List<DataScopeEntity> list = scopeMap.get(scopePO.getMethod());
            if (ObjectUtil.isEmpty(list)) {
                list = new ArrayList<>();
                list.add(scopePO);
                scopeMap.put(scopePO.getMethod(), list);
            } else {
                list.add(scopePO);
            }
        }
        return scopeMap;
    }
}
