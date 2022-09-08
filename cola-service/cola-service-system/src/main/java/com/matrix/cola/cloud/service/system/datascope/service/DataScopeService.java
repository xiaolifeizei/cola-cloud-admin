package com.matrix.cola.cloud.service.system.datascope.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 数据权限实体服务接口
 *
 * @author : cui_feng
 * @since : 2022-06-06 11:27
 */
public interface DataScopeService extends BaseColaEntityService<DataScopeEntity> {

    /**
     * 删除数据权限，物理删除
     * @param dataScopePO 数据权限对象，必须包含id
     * @return 返回统一对象
     */
    Result deleteDataScope(DataScopeEntity dataScopePO);

    /**
     * 获取数据权限映射
     * @return HashMap:key:method,value:数据权限对象
     */
    HashMap<String, List<DataScopeEntity>> getDataScopeMap();

}
