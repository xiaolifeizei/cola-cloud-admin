package com.matrix.cola.cloud.api.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * 公共实体类
 *
 * @author : cui_feng
 * @since : 2022-04-07 11:26
 */
@Data
public abstract class BaseColaEntity extends BaseEntity {

    /**
     * id号
     * 默认数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *修改人
     */
    private Long reviser;

    /**
     * 修改时间
     */
    private Date reviseTime;

    /**
     * 是否删除，1=删除，0=未删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 所属机构
     */
    private String groupId;
}
