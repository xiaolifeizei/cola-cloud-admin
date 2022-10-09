package com.matrix.cola.cloud.api.common.feign;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign异常处理父类
 *
 * @author : cui_feng
 * @since : 2022-10-09 17:26
 */
public abstract class AbstractFeignFallbackFactory<T extends BaseEntity> implements BaseColaFeign<T> {

    @Override
    public T getOne(@RequestParam Long id) {
        return null;
    }

    @Override
    public T getOne(@RequestBody T entity) {
        return null;
    }

    @Override
    public T getOne(@RequestBody Query<T> query) {
        return null;
    }

    @Override
    public List<T> getEntityList(@RequestBody Query<T> query) {
        return null;
    }

    @Override
    public Result add(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }

    @Override
    public Result update(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }

    @Override
    public Result delete(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }
}
