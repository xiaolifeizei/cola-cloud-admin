package com.matrix.cola.cloud.common.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.BaseEntityWrapper;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.api.common.service.BaseEntityWrapperService;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.QueryUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 实体包装服务类父类
 *
 * @author : cui_feng
 * @since : 2022-05-19 12:23
 */
public abstract class AbstractEntityWrapperService<T extends BaseEntity,W extends BaseEntityWrapper,S extends BaseEntityService<T>> implements BaseEntityWrapperService<T, W> {

    @Autowired
    protected CacheProxy cacheProxy;

    @Autowired(required = false)
    S service;

    protected S getService(){
        return service;
    }

    /**
     * 此方法用于填充实体对象中的通用字段，此方法会先调用entityWrapper方法
     * @param entity 实体对象
     * @return 实体包装类
     */
    @Override
    public W baseEntityWrapper(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return null;
        }
        W q = entityWrapper(entity);
        // 拷贝属性
        BeanUtils.copyProperties(entity,q);
        return q;
    }

    public W entityWrapper(T entity) {
        return getWrapperInstance();
    }

    private W getWrapperInstance() {
        try {
            Class<W> entityClass = (Class<W>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            return entityClass.newInstance();
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return null;
    }

    @Override
    public List<W> getWrapperList(Query<T> query) {
        return entityListWrapper(getService().getList(new QueryUtil<>(query).getWrapper()));
    }

    @Override
    public IPage<W> getWrapperPage(Query<T> query) {
        QueryUtil<T> queryUtil = new QueryUtil<>(query);
        return entityPageWrapper(getService().getPage(queryUtil.getPage(),queryUtil.getWrapper()));
    }

    @Override
    public List<W> getWrapperList(Wrapper<T> wrapper) {
        return entityListWrapper(getService().getList(wrapper));
    }

    @Override
    public IPage<W> getWrapperPage(IPage<T> page,Wrapper<T> wrapper) {
        return entityPageWrapper(getService().getPage(page,wrapper));
    }
}
