package com.matrix.cola.cloud.service.system.datalog.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogService;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogWrapperService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志Controller
 *
 * @author : cui_feng
 * @since : 2022-07-01 10:54
 */
@RestController
@RequestMapping("/dataLog")
public class DataLogController extends AbstractColaController<DataLogEntity, DataLogEntityWrapper, DataLogService, DataLogWrapperService> {


    public DataLogController(DataLogService service, DataLogWrapperService wrapperService) {
        super(service, wrapperService);
    }

    @Override
    @PostMapping("/deleteDataLog")
    @PreAuthorize("hasAuthority('administrator')")
    public Result delete(@RequestBody DataLogEntity dataLogEntity) {
        return service.deleteDataLog(dataLogEntity);
    }

    @PostMapping("/clearDataLog")
    @PreAuthorize("hasAuthority('administrator')")
    public Result clearDataLog() {
        return service.clearDataLog();
    }

    @PostMapping("/addUpdateLog")
    public void addUpdateLog(@RequestBody DataLogEntity dataLogEntity) {
        service.addUpdateLog(dataLogEntity);
    }

    @PostMapping("/addDeleteLog")
    public void addDeleteLog(@RequestBody DataLogEntity dataLogEntity) {
        service.addDeleteLog(dataLogEntity);
    }
}
