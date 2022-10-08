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
    default T getOne(@RequestParam Long id){
        return null;
    }

    @PostMapping("/getOne")
    default T getOne(@RequestBody T entity) {
        return null;
    }

    @PostMapping("/getOneByQuery")
    default T getOne(@RequestBody Query<T> query) {
        return null;
    }

    @PostMapping("/getEntityList")
    default List<T> getEntityList(@RequestBody Query<T> query) {
        return null;
    }

    @PostMapping("/add")
    default Result add(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }

    @PostMapping("/update")
    default Result update(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }

    @PostMapping("/delete")
    default Result delete(@RequestBody T entity) {
        return Result.err("接口调用失败");
    }
}
