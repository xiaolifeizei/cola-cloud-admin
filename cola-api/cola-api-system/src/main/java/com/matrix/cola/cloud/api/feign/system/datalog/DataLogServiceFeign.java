package com.matrix.cola.cloud.api.feign.system.datalog;

import com.matrix.cola.cloud.api.common.feign.BaseColaFeign;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 系统错误日志
 *
 * @author : cui_feng
 * @since : 2022-09-08 11:07
 */
@FeignClient(value = "cola-system",contextId = "system-dataLog-client", path = "dataLog",fallbackFactory = DataLogFeignFallbackFactory.class)
public interface DataLogServiceFeign extends BaseColaFeign<ErrorLogEntity> {

    @PostMapping("/addUpdateLog")
    void addUpdateLog(@RequestBody DataLogEntity dataLogEntity);

    @PostMapping("/addDeleteLog")
    void addDeleteLog(@RequestBody DataLogEntity dataLogEntity);
}
