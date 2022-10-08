package com.matrix.cola.cloud.api.entity.system.notice;

import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;
import lombok.Data;

/**
 * 通知公告包装类
 *
 * @author : cui_feng
 * @since : 2022-06-29 11:00
 */
@Data
public class NoticeEntityWrapper extends BaseColaEntityWrapper {

    /**
     * 标题
     */
    private String title;

    /**
     * 通知类型，0=通知，1=公告
     */
    private Integer noticeType;

    /**
     * 状态，0=关闭，1=正常
     */
    private Integer state;

    /**
     * 通知公告日期
     */
    private String noticeDate;

    /**
     * 通知内容
     */
    private String content;
}
