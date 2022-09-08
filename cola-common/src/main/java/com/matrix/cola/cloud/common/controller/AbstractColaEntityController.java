package com.matrix.cola.cloud.common.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * controller父类
 *
 * @author : cui_feng
 * @since : 2022-09-07 14:12
 */
public abstract class AbstractColaEntityController<T extends BaseColaEntity,S extends BaseColaEntityService<T>> {

    protected final S service;

    public AbstractColaEntityController(S service) {
        this.service = service;
    }

    @PostMapping("/getOneById")
    public T getOne(Long id) {
        return service.getOne(id);
    }

    @PostMapping("/getOne")
    public T getOne(@RequestBody T entity) {
        return service.getOne(entity);
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
    public Result addUser(@RequestBody T entity) {
        return service.insert(entity);
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody T entity) {
        return service.modify(entity);
    }

    @PostMapping("/delete")
    public Result deleteUser(@RequestBody T entity) {
        return service.remove(entity);
    }

}
