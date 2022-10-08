package com.matrix.cola.cloud.service.system.dict.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.dict.DictEntity;
import com.matrix.cola.cloud.common.controller.AbstractEntityController;
import com.matrix.cola.cloud.service.system.dict.service.DictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典管理Controller
 *
 * @author : cui_feng
 * @since : 2022-05-18 12:57
 */
@RestController
@RequestMapping("/dict")
public class DictController extends AbstractEntityController<DictEntity, DictService> {


    public DictController(DictService service) {
        super(service);
    }

    @PostMapping("/getTree")
    public Result getDictTree(@RequestBody Query<DictEntity> query) {
        return service.getDictTree(query);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')")
    public Result delete(@RequestBody DictEntity dictPO) {
        return service.deleteDict(dictPO);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')")
    public Result update(@RequestBody DictEntity dictPO) {
        return super.update(dictPO);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')")
    public Result add(@RequestBody DictEntity dictPO) {
        return super.add(dictPO);
    }
}
