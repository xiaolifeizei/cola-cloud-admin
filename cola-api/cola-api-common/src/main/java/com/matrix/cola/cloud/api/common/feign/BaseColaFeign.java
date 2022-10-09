package com.matrix.cola.cloud.api.common.feign;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 */
public interface BaseColaFeign<T extends BaseEntity> {

    @PostMapping("/getOneById")
    T getOne(@RequestParam Long id);

    @PostMapping("/getOne")
    T getOne(@RequestBody T entity);

    @PostMapping("/getOneByQuery")
    T getOne(@RequestBody Query<T> query);

    @PostMapping("/getEntityList")
    List<T> getEntityList(@RequestBody Query<T> query);

    @PostMapping("/add")
    Result add(@RequestBody T entity);

    @PostMapping("/update")
    Result update(@RequestBody T entity);

    @PostMapping("/delete")
    Result delete(@RequestBody T entity);
}
