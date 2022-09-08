package com.matrix.cola.cloud.api.entity.system.group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import lombok.Data;

/**
 * 组织机构实体类
 *
 * @author : cui_feng
 * @since : 2022-05-30 14:56
 */
@Data
@TableName("system_group")
public class GroupEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构简称
     */
    private String shortName;

    /**
     * 地址
     */
    private String address;

    /**
     * 机构类型：0=集团公司，1=分公司，2=部门，3=岗位
     */
    private Integer groupType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 备注
     */
    private String remark;

    /**
     * 祖籍列表
     */
    private String ancestors;

}
