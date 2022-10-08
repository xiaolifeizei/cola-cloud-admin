package com.matrix.cola.cloud.service.system.datalog.service.impl;

import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntityWrapper;
import com.matrix.cola.cloud.common.service.AbstractColaEntityWrapperService;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogService;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogWrapperService;
import org.springframework.stereotype.Service;

/**
 * 系统日志包装类接口实现类
 *
 * @author : cui_feng
 * @since : 2022-07-01 10:58
 */
@Service
public class DataLogWrapperServiceImpl extends AbstractColaEntityWrapperService<DataLogEntity, DataLogEntityWrapper, DataLogService> implements DataLogWrapperService {


    @Override
    public DataLogEntityWrapper entityWrapper(DataLogEntity entity) {
        return new DataLogEntityWrapper();
    }
}
