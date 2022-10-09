package com.matrix.cola.cloud.api.feign.system.datalog;

import com.matrix.cola.cloud.api.common.feign.AbstractFeignFallbackFactory;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据日志Fallback
 *
 * @author : cui_feng
 * @since : 2022-10-08 16:41
 */
@Component
@Slf4j
public class DataLogFeignFallbackFactory extends AbstractFeignFallbackFactory<ErrorLogEntity> implements DataLogServiceFeign,FallbackFactory<DataLogServiceFeign> {

    @Override
    public DataLogServiceFeign create(Throwable cause) {
        cause.printStackTrace();
        log.error(cause.getMessage());
        return new DataLogFeignFallbackFactory();
    }

    @Override
    public void addUpdateLog(DataLogEntity dataLogEntity) {

    }

    @Override
    public void addDeleteLog(DataLogEntity dataLogEntity) {

    }
}
