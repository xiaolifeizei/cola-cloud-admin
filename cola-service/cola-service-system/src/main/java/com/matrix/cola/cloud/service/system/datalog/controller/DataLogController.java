package com.matrix.cola.cloud.service.system.datalog.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogService;
import com.matrix.cola.cloud.service.system.datalog.service.DataLogWrapperService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Result addUpdateLog(@RequestParam String tableName, @RequestParam BaseColaEntity before, @RequestParam BaseColaEntity after) {
        service.addUpdateLog(tableName, before, after);
        return Result.ok();
    }

    @PostMapping("/addDeleteLog")
    public Result addDeleteLog(@RequestParam String tableName, @RequestParam BaseColaEntity before) {
        service.addDeleteLog(tableName, before);
        return Result.ok();
    }
}
