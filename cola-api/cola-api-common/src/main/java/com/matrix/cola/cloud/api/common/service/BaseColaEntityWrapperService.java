package com.matrix.cola.cloud.api.common.service;

import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;

/**
 * 实体包装类接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface BaseColaEntityWrapperService<T extends BaseColaEntity,W extends BaseColaEntityWrapper> extends BaseEntityWrapperService<T,W> {

    /**
     * 获取订单包装类
     * @param id 实体类主键
     * @return 查询实体类
     */
    W getWrapperOne(Long id);

    /**
     * 通过实体类获取实体类的包装类
     * @param entity 实体对象，继承自BasePO
     * @return 查询实体类，继承自BaseQO
     */
    W getWrapperOne(T entity);
}
