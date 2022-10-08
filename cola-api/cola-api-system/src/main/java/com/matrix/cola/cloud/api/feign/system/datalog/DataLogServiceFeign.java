package com.matrix.cola.cloud.api.feign.system.datalog;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.feign.BaseColaFeign;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统错误日志
 *
 * @author : cui_feng
 * @since : 2022-09-08 11:07
 */
@FeignClient(value = "cola-system",contextId = "system-dataLog-client", path = "dataLog",fallbackFactory = DataLogFeignFallbackFactory.class)
public interface DataLogServiceFeign extends BaseColaFeign<ErrorLogEntity> {

    @PostMapping("/addUpdateLog")
    Result addUpdateLog(@RequestParam String tableName, @RequestParam BaseColaEntity before, @RequestParam BaseColaEntity after);

    @PostMapping("/addDeleteLog")
    Result addDeleteLog(@RequestParam String tableName, @RequestParam BaseColaEntity before);
}
