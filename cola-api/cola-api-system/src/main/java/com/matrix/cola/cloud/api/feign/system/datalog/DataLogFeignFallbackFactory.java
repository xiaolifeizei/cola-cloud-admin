package com.matrix.cola.cloud.api.feign.system.datalog;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 数据日志Fallback
 *
 * @author : cui_feng
 * @since : 2022-10-08 16:41
 */
@Component
public class DataLogFeignFallbackFactory implements FallbackFactory<DataLogServiceFeign> {

    @Override
    public DataLogServiceFeign create(Throwable cause) {
        return new DataLogServiceFeign() {

            @Override
            public Result addUpdateLog(String tableName, BaseColaEntity before, BaseColaEntity after) {
                return Result.ok();
            }

            @Override
            public Result addDeleteLog(String tableName, BaseColaEntity before) {
                System.out.println(cause);
                return Result.ok();
            }
        };
    }
}
