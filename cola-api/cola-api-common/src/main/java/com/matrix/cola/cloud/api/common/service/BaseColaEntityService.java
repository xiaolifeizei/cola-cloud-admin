package com.matrix.cola.cloud.api.common.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;

import java.util.List;

/**
 * 实体类服务接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface BaseColaEntityService<T extends BaseColaEntity> extends BaseEntityService<T> {

    /**
     * 根据id号获取一条记录
     *
     * @param entity 包含id的实体类
     * @return 实体类对象
     */
    T getOne(T entity);

    /**
     * 根据id号获取一条记录
     *
     * @param id 实体id
     * @return 实体类对象
     */
    T getOne(Long id);

    /**
     * 根据id号删除一条记录
     * 逻辑删除
     *
     * @param id 实体id号
     * @return 执行成功的条数
     */
     Integer delete(Long id);


    /**
     * 根据id号批量删除
     * 逻辑删除
     * @param idList id列表
     * @return 执行成功的条数
     */
     Integer batchDeleteByIds(List<Long> idList);

    /**
     * 根据id号删除一条记录，返回通用类型
     * @param id 实体id号
     * @return 通用返回类型
     */
     Result remove(Long id);

    /**
     * 根据id号批量删除，返回通用类型
     * @param idList id列表
     * @return 通用返回类型
     */
     Result batchRemoveByIds(List<Long> idList);
}
