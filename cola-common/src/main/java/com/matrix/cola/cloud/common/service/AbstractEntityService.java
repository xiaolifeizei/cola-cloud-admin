package com.matrix.cola.cloud.common.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * 抽象实体服务类
 *
 * @author : cui_feng
 * @since : 2022-04-07 13:26
 */
public abstract class AbstractEntityService<T extends BaseEntity,M extends BaseMapper<T>> implements BaseEntityService<T> {

    @Autowired
    protected CacheProxy cacheProxy;

    @Autowired(required = false)
    M mapper;

    /**
     * 获取Mapper依赖
     * @return {@link BaseMapper} 的子接口
     */
    protected M getMapper() {
        return mapper;
    }


    @Override
    public T getOne(Wrapper<T> queryWrapper) {
        List<T> list = getList(queryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            if (list.size() > 1) {
                throw new RuntimeException("查询失败，您希望查询出1个对象，但是查询出了" + list.size() + "个对象");
            }
            afterQuery(list);
            return list.get(0);
        }
        return null;
    }

    @Override
    public T getOne(Query<T> query) {
        QueryUtil<T> queryUtil = new QueryUtil<>(query);
        return getOne(queryUtil.getWrapper());
    }

    @Override
    public List<T> getList(Query<T> query) {
        List<T> list = getList(new QueryUtil<T>(query).getWrapper());
        afterQuery(list);
        return list;
    }

    @Override
    public List<T> getList(Wrapper<T> queryWrapper) {
        List<T> list = getMapper().selectList(queryWrapper);
        afterQuery(list);
        return list;
    }

    @Override
    public IPage<T> getPage(Query<T> query) {
        QueryUtil<T> queryUtil = new QueryUtil<T>(query);
        return getPage(queryUtil.getPage(),queryUtil.getWrapper());
    }

    @Override
    public IPage<T> getPage(IPage<T> page,Wrapper<T> queryWrapper) {
        IPage<T> pages = getMapper().selectPage(page,queryWrapper);
        if (ObjectUtil.isNotNull(pages)) {
            afterQuery(pages.getRecords());
        }
        return page;
    }

    @Override
    public Long getCount(Wrapper<T> queryWrapper) {
        return getMapper().selectCount(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return ColaConstant.NO;
        }
        if (!validate(entity).isSuccess() || !beforeAdd(entity).isSuccess()) {
            return ColaConstant.NO;
        }
        int count = getMapper().insert(entity);
        if (count == ColaConstant.YES) {
            if (!afterAdd(entity).isSuccess()) {
                rollback();
                return ColaConstant.NO;
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAdd(List<T> entityList) {
        if (ObjectUtil.isEmpty(entityList)) {
            return ColaConstant.NO;
        }
        Integer count = 0;
        for (T entity : entityList) {
            if (add(entity) != ColaConstant.YES) {
                rollback();
                return count;
            }
            count ++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchInsert(List<T> entityList) {
        if (ObjectUtil.isEmpty(entityList)) {
            return Result.err("批量添加失败，实体对象集合不能为空");
        }
        for (T entity : entityList) {
            Result result =  insert(entity);
            if (!result.isSuccess()) {
                rollback();
                return result;
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insert(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return Result.err("添加失败，实体类对象不能为空");
        }
        Result result = validate(entity);
        if(!result.isSuccess()) {
            return result;
        }
        result = beforeAdd(entity);
        if (!result.isSuccess()) {
            return result;
        }
        if (getMapper().insert(entity) == ColaConstant.YES) {
            result = afterAdd(entity);
            if (result.isSuccess()) {
                return Result.ok("添加成功");
            } else {
                rollback();
                return result;
            }
        }
        return Result.err("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return ColaConstant.NO;
        }
        if (!validate(entity).isSuccess() || !beforeUpdate(entity).isSuccess()) {
            return ColaConstant.NO;
        }
        int count = getMapper().updateById(entity);
        if (count == ColaConstant.YES) {
            if (!afterUpdate(entity).isSuccess()) {
                rollback();
                return ColaConstant.NO;
            }
        }
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modify(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return Result.err("修改失败，实体对象不能为空");
        }
        Result result = validate(entity);
        if (!result.isSuccess()) {
            return result;
        }
        result = beforeUpdate(entity);
        if (!result.isSuccess()) {
            return result;
        }
        if (getMapper().updateById(entity) == ColaConstant.YES) {
            result = afterUpdate(entity);
            if (result.isSuccess()) {
                return Result.ok("修改成功");
            } else {
                rollback();
                return result;
            }
        }
        return Result.err("修改失败");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return ColaConstant.NO;
        }
        if (!beforeDelete(entity).isSuccess()) {
            return ColaConstant.NO;
        }

        int count = getMapper().deleteById(entity);
        if (count != ColaConstant.YES) {
            rollback();
            return ColaConstant.NO;
        }

        if (!afterDelete(entity).isSuccess()) {
            rollback();
            return ColaConstant.NO;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDelete(List<T> entityList) {
        if (ObjectUtil.isEmpty(entityList)) {
            return ColaConstant.NO;
        }
        Integer count = 0;
        for(T entity : entityList) {
            if (delete(entity) != ColaConstant.YES) {
                rollback();
                return count;
            }
            count ++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result remove(T entity) {
        if (ObjectUtil.isNull(entity)) {
            return Result.err("删除失败，参数不能为空");
        }
        Result result = beforeDelete(entity);
        if (!result.isSuccess()){
            return result;
        }
        if (getMapper().deleteById(entity) != ColaConstant.YES) {
            return Result.err("删除失败");
        }
        result = afterDelete(entity);
        if (!result.isSuccess()) {
            rollback();
            return result;
        }
        return Result.ok("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchRemove(List<T> entityList) {
        if (ObjectUtil.isEmpty(entityList)) {
            return Result.err("批量删除失败，实体类列表不能为空");
        }
        for (T entity : entityList) {
            Result result = remove(entity);
            if (!result.isSuccess()) {
                rollback();
                return result;
            }
        }
        return Result.ok();
    }

    /**
     * 手动回滚事务<br>
     * 用此方法时，方法前必须要使用@Transactional注解声明
     */
    protected void rollback() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    /**
     * 查询后执行的方法，用于修改查询结果
     * @param entityList 实体类列表
     */
    protected void afterQuery(List<T> entityList) {
    }
    /**
     * 添加之前要执行的方法
     * @param entity 实体类
     * @return 返回通用类型
     */
    protected Result beforeAdd(T entity){
        return Result.ok();
    }

    /**
     * 添加完成之后执行的方法（事务内）
     * @param entity 实体对象
     * @return 返回通用类型
     */
    protected Result afterAdd(T entity) {
        return Result.ok();
    }

    /**
     * 修改之前执行的方法
     * @param entity 实体类
     * @return 返回通用类型
     */
    protected Result beforeUpdate(T entity) {
        return Result.ok();
    }

    /**
     * 修改之后执行的方法（事务内）
     * @param entity 实体类
     * @return 返回通用类型
     */
    protected Result afterUpdate(T entity) {
        return Result.ok();
    }

    /**
     * 删除前调用的方法
     * @param entity 实体类
     * @return 返回通用类型
     */
    protected Result beforeDelete(T entity) {
        return Result.ok();
    }

    /**
     * 删除之后调用的方法（事务内）
     * @param entity 实体类
     * @return 返回通用类型
     */
    protected Result afterDelete(T entity){
        return Result.ok();
    }

    /**
     * 实体类验证方法，调用添加和修改方法时会先调用此方法进行验证，验证成功后才会执行后面的操作
     * @param entity 实体对象
     * @return 返回统一类型
     */
    protected Result validate(T entity) {
        return Result.ok();
    }
}
