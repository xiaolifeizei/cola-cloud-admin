package com.matrix.cola.cloud.service.system.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.notice.NoticeEntity;
import com.matrix.cola.cloud.common.service.AbstractColaEntityService;
import com.matrix.cola.cloud.service.system.notice.mapper.NoticeMapper;
import com.matrix.cola.cloud.service.system.notice.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * 通知公告实体类接口实现类
 *
 * @author : cui_feng
 * @since : 2022-06-29 11:04
 */
@Service
public class NoticeServiceImpl extends AbstractColaEntityService<NoticeEntity, NoticeMapper> implements NoticeService {

    @Override
    protected Result validate(NoticeEntity po) {
        if (StrUtil.isEmpty(po.getTitle())) {
            return Result.err("操作失败，标题不能为空");
        }

        if (StrUtil.isEmpty(po.getNoticeDate())) {
            return Result.err("操作失败，通知公告日期不能为空");
        }

        if (ObjectUtil.isNull(po.getNoticeType())) {
            return Result.err("操作失败，通知类型不能为空");
        }

        if (StrUtil.isEmpty(po.getContent())) {
            return Result.err("操作失败，通知内容不能为空");
        }

        return super.validate(po);
    }
}
