package com.matrix.cola.cloud.common.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * controller父类
 * 封装了Entity的增删改查方法
 *
 * @author : cui_feng
 * @since : 2022-09-07 14:12
 */
public abstract class AbstractEntityController<T extends BaseEntity,S extends BaseEntityService<T>> {

    protected final S service;

    public AbstractEntityController(S service) {
        this.service = service;
    }

    @PostMapping("/getOne")
    public T getOne(@RequestBody Query<T> query) {
        return service.getOne(query);
    }

    @PostMapping("/getPage")
    public Result getPage(@RequestBody Query<T> query) {
        return Result.page(service.getPage(query));
    }

    @PostMapping("/getList")
    public Result getList(@RequestBody Query<T> query) {
        return Result.list(service.getList(query));
    }

    @PostMapping("/getEntityList")
    public List<T> getEntityList(@RequestBody Query<T> query) {
        return service.getList(query);
    }

    @PostMapping("/add")
    public Result add(@RequestBody T entity) {
        return service.insert(entity);
    }

    @PostMapping("/update")
    public Result update(@RequestBody T entity) {
        return service.modify(entity);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody T entity) {
        return service.remove(entity);
    }

}
