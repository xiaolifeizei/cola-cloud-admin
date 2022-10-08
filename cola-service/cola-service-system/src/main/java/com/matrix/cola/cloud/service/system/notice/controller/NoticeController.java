package com.matrix.cola.cloud.service.system.notice.controller;

import com.matrix.cola.cloud.api.entity.system.notice.NoticeEntity;
import com.matrix.cola.cloud.api.entity.system.notice.NoticeEntityWrapper;
import com.matrix.cola.cloud.common.controller.AbstractColaController;
import com.matrix.cola.cloud.service.system.notice.service.NoticeService;
import com.matrix.cola.cloud.service.system.notice.service.NoticeWrapperService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知公告Controller
 *
 * @author : cui_feng
 * @since : 2022-06-29 11:12
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends AbstractColaController<NoticeEntity, NoticeEntityWrapper, NoticeService, NoticeWrapperService> {

    public NoticeController(NoticeService service, NoticeWrapperService wrapperService) {
        super(service, wrapperService);
    }
}
