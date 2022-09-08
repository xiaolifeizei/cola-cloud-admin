package com.matrix.cola.cloud.api.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 实体包装类
 *
 * @author : cui_feng
 * @since : 2022-04-11 09:09
 */
@Data
public abstract class BaseColaEntityWrapper extends BaseEntityWrapper {

    /**
     * id号
     * 默认数据库自增
     */
    private Long id;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建人
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     *修改人
     */
    private Long reviser;

    /**
     * 修改人
     */
    private String reviserName;
    /**
     * 修改时间
     */
    private Date reviseTime;

    /**
     * 是否删除，1=删除，0=未删除
     */
    private Integer deleted;

    /**
     * 是否删除
     */
    private String showDeleted;

    /**
     * 所属机构
     */
    private String groupId;

    /**
     * 所属机构名称
     */
    private String groupName;
}
