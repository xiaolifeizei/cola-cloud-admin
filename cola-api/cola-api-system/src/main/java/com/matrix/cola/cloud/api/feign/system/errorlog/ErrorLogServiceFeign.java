package com.matrix.cola.cloud.api.feign.system.errorlog;

import com.matrix.cola.cloud.api.common.feign.BaseColaFeign;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 系统错误日志
 *
 * @author : cui_feng
 * @since : 2022-09-08 11:07
 */
@FeignClient(value = "cola-system",contextId = "system-errorLog-client", path = "errorLog")
public interface ErrorLogServiceFeign extends BaseColaFeign<ErrorLogEntity> {
}
