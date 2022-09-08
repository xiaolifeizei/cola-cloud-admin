package com.matrix.cola.cloud.api.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.BaseEntityWrapper;
import com.matrix.cola.cloud.api.common.entity.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体包装类接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface BaseEntityWrapperService<T extends BaseEntity,W extends BaseEntityWrapper> extends BaseService {

    /**
     * 包装实体类
     * @param entity 实体类
     * @return 实体包装类
     */
    W entityWrapper(T entity);

    /**
     * 包装实体类基础字段
     * @param entity 实体对象
     * @return 实体包装类
     */
    W baseEntityWrapper(T entity);

    /**
     * 将实体类列表转换成实体包装类列表
     *
     * @param entityList 实体列表
     * @return ArrayList 实体包装类列表
     */
    default List<W> entityListWrapper(List<T> entityList) {
        if (entityList != null && entityList.size()>0) {
            List<W> list = new ArrayList<>();
            for (T entity : entityList) {
                list.add(baseEntityWrapper(entity));
            }
            return list;
        }
        return null;
    }

    /**
     * 包装分页实体列表
     *
     * @param page 实体类分页对象
     * @return 实体包装类分页对象
     */
    default IPage<W> entityPageWrapper(IPage<T> page) {
        IPage<W> wrapperPage = new Page<>();
        if (page!=null && page.getRecords()!=null && page.getRecords().size()>0) {
            List<W> records = new ArrayList<>();
            for (T entity : page.getRecords()) {
                records.add(baseEntityWrapper(entity));
            }
            wrapperPage.setCurrent(page.getCurrent());
            wrapperPage.setPages(page.getPages());
            wrapperPage.setRecords(records);
            wrapperPage.setSize(page.getSize());
            wrapperPage.setTotal(page.getTotal());
            return wrapperPage;
        }
        return null;
    }

    /**
     * 查询实体包装类集合
     * @param query 查询对象
     * @return ArrayList
     */
    List<W> getWrapperList(Query<T> query);

    /**
     * 查询实体包装类集合
     * @param wrapper 查询条件
     * @return ArrayList
     */
    List<W> getWrapperList(Wrapper<T> wrapper);

    /**
     * 实体包装类的分页查询
     * @param query 查询对象
     * @return 分页的查询实体类 IPage
     */
    IPage<W> getWrapperPage(Query<T> query);

    /**
     * 实体包装类的分页查询
     * @param wrapper 查询条件
     * @return 分页的查询实体类 IPage
     */
    IPage<W> getWrapperPage(IPage<T> page, Wrapper<T> wrapper);
}
