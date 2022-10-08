package com.matrix.cola.cloud.service.system.datascope.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeService;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeWrapperService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据权限Controller
 *
 * @author : cui_feng
 * @since : 2022-06-06 11:30
 */
@RestController
@RequestMapping("/dataScope")
public class DataScopeController extends AbstractColaController<DataScopeEntity, DataScopeEntityWrapper, DataScopeService, DataScopeWrapperService> {


    public DataScopeController(DataScopeService service, DataScopeWrapperService wrapperService) {
        super(service, wrapperService);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')")
    public Result getList(@RequestBody Query<DataScopeEntity> query) {
        return Result.list(wrapperService.getWrapperList(query));
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')")
    public Result add(@RequestBody DataScopeEntity dataScopePO) {
        return service.insert(dataScopePO);
    }

    @PreAuthorize("hasAuthority('administrator')")
    public Result update(@RequestBody DataScopeEntity dataScopePO) {
        return service.modify(dataScopePO);
    }

    @PreAuthorize("hasAuthority('administrator')")
    public Result delete(@RequestBody DataScopeEntity dataScopePO) {
        return service.deleteDataScope(dataScopePO);
    }
}
