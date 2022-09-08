package com.matrix.cola.cloud.api.entity.system.menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import lombok.Data;

/**
 * 菜单实体类
 *
 * @author : cui_feng
 * @since : 2022-05-11 14:22
 */
@Data
@TableName("system_menu")
public class MenuEntity extends BaseEntity {

    /**
     * id号
     * 默认数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 父菜单
     */
    private Long parentId;

    /**
     * 菜单url
     */
    private String url;

    /**
     * 组件
     */
    private String component;

    /**
     * 菜单类型：0=菜单，1=按钮
     */
    private Integer menuType;

    /**
     * 打开方式：0=页面内，1=新窗口
     */
    private Integer openType;

    /**
     *图标
     */
    private String icon;

    /**
     * 不可见：0=可见，1=不可见
     */
    private Integer hidden;

    /**
     * 菜单排序
     */
    private Integer orders;


}
