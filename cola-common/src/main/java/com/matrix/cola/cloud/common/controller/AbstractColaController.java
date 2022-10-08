package com.matrix.cola.cloud.common.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityWrapperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * controller父类
 * 封装EntityWrapper的查询和ColaEntity的增删改查方法
 *
 * @author : cui_feng
 * @since : 2022-09-07 17:11
 */
public class AbstractColaController<T extends BaseColaEntity,W extends BaseColaEntityWrapper,S extends BaseColaEntityService<T>, WS extends BaseColaEntityWrapperService<T,W>> extends AbstractColaEntityController<T,S>{

    protected final WS wrapperService;

    public AbstractColaController(S service,WS wrapperService) {
        super(service);
        this.wrapperService = wrapperService;
    }

    @PostMapping("/getWrapperPage")
    public Result getWrapperPage(@RequestBody Query<T> query) {
        return Result.page(wrapperService.getWrapperPage(query));
    }

    @PostMapping("/getWrapperOne")
    public W getWrapperOne(@RequestBody T entity) {
        return wrapperService.getWrapperOne(entity);
    }

    @PostMapping("/getWrapperOneById")
    public W getWrapperOneById(long id) {
        return wrapperService.getWrapperOne(id);
    }
}
