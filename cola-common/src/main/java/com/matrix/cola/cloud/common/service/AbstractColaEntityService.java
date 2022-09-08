package com.matrix.cola.cloud.common.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.utils.WebUtil;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 抽象实体服务类
 *
 * @author : cui_feng
 * @since : 2022-04-07 13:26
 */
public abstract class AbstractColaEntityService<T extends BaseColaEntity,M extends BaseMapper<T>> extends AbstractEntityService<T,M> implements BaseColaEntityService<T> {

    @Override
    public T getOne(T entity) {
        if (ObjectUtil.isNull(entity) || ObjectUtil.isNull(entity.getId())) {
            afterQuery(null);
            return null;
        }
        return getOne(entity.getId());
    }

    @Override
    public T getOne(Long id) {
        if (ObjectUtil.isNull(id)) {
            afterQuery(null);
            return null;
        }
        T result = (T)getMapper().selectById(id);
        List<T> list = new ArrayList<>();
        if (ObjectUtil.isNotNull(result)) {
            list.add(result);
        }
        afterQuery(list);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return ColaConstant.NO;
        }
        if (!fillAdd(entity).isSuccess()) {
            return ColaConstant.NO;
        }
        return super.add(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insert(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return Result.err("添加失败，实体类对象不能为空");
        }
        Result result = fillAdd(entity);
        if (!result.isSuccess()) {
            return result;
        }
        return super.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(T entity) {
        if (ObjectUtil.isNull(entity) || ObjectUtil.isNull(entity.getId())) {
            return ColaConstant.NO;
        }
        fillUpdate(entity);
        return super.update(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modify(T entity) {
        if (ObjectUtil.isNull(entity) || ObjectUtil.isNull(entity.getId())) {
            return Result.err("修改失败，实体对象或实体对象的id不能为空");
        }
        fillUpdate(entity);
        return super.modify(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Long id) {
        if (ObjectUtil.isNull(id)) {
            return ColaConstant.NO;
        }

        T entity = getEntityInstance();
        if (ObjectUtil.isNull(entity)) {
            return ColaConstant.NO;
        }
        entity.setId(id);
        return super.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteByIds(List<Long> idList) {
        if (ObjectUtil.isEmpty(idList)) {
            return ColaConstant.NO;
        }
        List<T> entityList = new ArrayList<>();
        for (Long id : idList) {
            T entity = getEntityInstance();
            if (ObjectUtil.isNull(entity)) {
                return ColaConstant.NO;
            }
            entity.setId(id);
            entityList.add(entity);
        }
        return super.batchDelete(entityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result remove(Long id) {
        if (ObjectUtil.isNull(id)) {
            return Result.err("删除失败，id不能为空");
        }
        T entity = getEntityInstance();
        if (ObjectUtil.isNull(entity)) {
            return Result.err("删除失败，没有获取到实体类对象");
        }
        entity.setId(id);
        return super.remove(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchRemoveByIds(List<Long> idList) {
        if (ObjectUtil.isEmpty(idList)) {
            return Result.err("批量删除失败，参数不能为空");
        }
        List<T> entityList = new ArrayList<>();
        for (Long id : idList) {
            T entity = getEntityInstance();
            if (ObjectUtil.isNull(entity)) {
                return Result.err("批量删除失败，没有获取到实体类对象");
            }
            entity.setId(id);
            entityList.add(entity);
        }
        return batchRemove(entityList);
    }

    protected Result fillAdd(T entity) {
        entity.setCreateTime(new Date());
        entity.setReviseTime(new Date());
        entity.setDeleted(ColaConstant.NO);
        UserEntity userPO = WebUtil.getUser();
        if (StrUtil.isEmpty(entity.getGroupId())) {
            if (ObjectUtil.isNotNull(userPO)) {
                if (StrUtil.isEmpty(userPO.getGroupId())) {
                    return Result.err("添加失败，您不属于任何机构，无法添加业务数据");
                }
                if (userPO.getGroupId().split(",").length > 1) {
                    return Result.err("添加失败，您当前属于多个机构，无法添加业务数据");
                }
                entity.setGroupId(userPO.getGroupId());
            }
        }
        if (ObjectUtil.isNotNull(userPO)) {
            entity.setCreator(userPO.getId());
            entity.setReviser(userPO.getId());
        }
        return Result.ok();
    }

    private void fillUpdate(T entity) {
        entity.setReviseTime(new Date());
        UserEntity userPO = WebUtil.getUser();
        if (ObjectUtil.isNotNull(userPO)) {
            entity.setReviser(userPO.getId());
        }
    }

    /**
     * 获取泛型 class
     * @return 实体类对象
     */
    private T getEntityInstance() {
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return entityClass.newInstance();
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return null;
    }
}
