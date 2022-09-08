package com.matrix.cola.cloud.api.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;

import java.util.List;

/**
 * 实体类服务接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface BaseEntityService<T extends BaseEntity> extends BaseService {

    /**
     * 通过查询条件查询一个实体类，当有多个时抛出异常
     * @param queryWrapper 查询条件
     * @return 实体对象
     */
    T getOne(Wrapper<T> queryWrapper);

    /**
     * 通过查询条件查询一个实体类，当有多个时抛出异常
     * @param query 查询条件
     * @return 实体对象
     */
    T getOne(Query<T> query);

    /**
     * 根据前端查询条件获取集合
     * @param query 查询条件
     * @return 实体类集合
     */
    List<T> getList(Query<T> query);

    /**
     * 根据查询条件获取集合，此方法不能用在dubbo的消费端调用<br>
     * 在dubbo消费端请使用 {@link #getList(Query)}
     *
     * @param queryWrapper myBatis-plus提供的Wrapper接口子类{@link Wrapper}
     * @return 实体类集合
     */
    List<T> getList(Wrapper<T> queryWrapper);

    /**
     * 根据前端查询条件获取分页数据集
     *
     * @param query 查询条件
     * @return 实体集合的分页查询
     */
    IPage<T> getPage(Query<T> query);

    /**
     * 根据查询条件获取分页数据集合
     * 此方法不能用在dubbo的消费端<br>
     * 在dubbo消费端请使用 {@link #getPage(Query)}
     *
     * @param page 分页对象
     * @param queryWrapper 查询条件，myBatis-plus提供的Wrapper接口子类{@link Wrapper}
     * @return 实体类的分页对象
     */
    IPage<T> getPage(IPage<T> page,Wrapper<T> queryWrapper);

    /**
     * 根据查询条件查询条数
     *
     * @param queryWrapper，查询条件，myBatis-plus提供的Wrapper接口子类{@link Wrapper}
     * @return 记录数
     */
    Long getCount(Wrapper<T> queryWrapper);

    /**
     * 添加一条记录
     * @param entity 实体类
     * @return 执行成功的条数
     */
     Integer add(T entity);

    /**
     * 批量添加
     * @param entityList 实体对象集合
     * @return 执行成功的条数
     */
     Integer batchAdd(List<T> entityList);

    /**
     * 批量添加，返回通用类型
     * @param entityList 实体对象集合
     * @return 统一返回结果
     */
     Result batchInsert(List<T> entityList);

    /**
     * 添加一条记录，返回通用类型
     * @param entity 实体类
     * @return 统一返回结果
     */
     Result insert(T entity);

    /**
     * 根据id号修改一条记录
     * @param entity 包含id的实体类
     * @return 执行成功的条数
     */
     Integer update(T entity);

    /**
     * 根据id号修改一条记录，返回通用类型
     * @param entity 包含id的实体类
     * @return 通用返回类型
     */
     Result modify(T entity);


    /**
     * 删除实体对象
     * 逻辑删除
     *
     * @param entity 实体对象，必须有id
     * @return 执行成功的条数
     */
     Integer delete(T entity);

    /**
     * 批量删除
     * 逻辑删除
     * @param entityList 实体对象集合，实体对象必须要有id
     * @return 执行成功的条数
     */
     Integer batchDelete(List<T> entityList);

    /**
     * 删除实体对象，必须包含id
     * @param entity 实体对象
     * @return 通用返回类型
     */
     Result remove(T entity);

    /**
     * 批量删除
     * @param entityList 实体对象集合
     * @return 通用返回类型
     */
     Result batchRemove(List<T> entityList);
}
