package com.matrix.cola.cloud.service.system.notice.service.impl;

import com.matrix.cola.cloud.api.entity.system.notice.NoticeEntity;
import com.matrix.cola.cloud.api.entity.system.notice.NoticeEntityWrapper;
import com.matrix.cola.cloud.common.service.AbstractColaEntityWrapperService;
import com.matrix.cola.cloud.service.system.notice.service.NoticeService;
import com.matrix.cola.cloud.service.system.notice.service.NoticeWrapperService;
import org.springframework.stereotype.Service;

/**
 * 通知公告包装类接口实现类
 *
 * @author : cui_feng
 * @since : 2022-06-29 11:05
 */
@Service
public class NoticeWrapperServiceImpl extends AbstractColaEntityWrapperService<NoticeEntity, NoticeEntityWrapper, NoticeService> implements NoticeWrapperService {

}
