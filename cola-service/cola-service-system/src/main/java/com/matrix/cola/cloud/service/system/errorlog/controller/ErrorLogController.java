package com.matrix.cola.cloud.service.system.errorlog.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.errorlog.service.ErrorLogService;
import com.matrix.cola.cloud.service.system.errorlog.service.ErrorLogWrapperService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错误日志controller
 *
 * @author : cui_feng
 * @since : 2022-06-10 12:45
 */
@RequestMapping("/errorLog")
@RestController
public class ErrorLogController extends AbstractColaController<ErrorLogEntity, ErrorLogEntityWrapper,ErrorLogService,ErrorLogWrapperService> {

    public ErrorLogController(ErrorLogService errorLogService,ErrorLogWrapperService errorLogWrapper) {
        super(errorLogService, errorLogWrapper);
    }

    @PostMapping("/getErrorLogPage")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result getErrorLogPage(@RequestBody Query<ErrorLogEntity> query) {
        return Result.page(wrapperService.getWrapperPage(query));
    }

    @PostMapping("/clearErrorLog")
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result clearErrorLog() {
        return service.clearErrorLog();
    }
}
