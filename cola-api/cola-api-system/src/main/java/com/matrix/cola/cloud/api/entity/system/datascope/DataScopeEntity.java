package com.matrix.cola.cloud.api.entity.system.datascope;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import lombok.Data;

/**
 * 数据权限实体类
 *
 * @author : cui_feng
 * @since : 2022-06-06 11:24
 */
@Data
@TableName("system_data_scope")
public class DataScopeEntity extends BaseColaEntity {

    /**
     * 数据权限名称
     */
    private String name;

    /**
     * 关联的菜单id
     */
    private Long menuId;

    /**
     * 拦截的方法
     */
    private String method;

    /**
     * 数据权限类型，0=全部数据,1=本机构数据,2=本机构及下级机构,3=本人数据4,自定义
     */
    private Integer scopeType;

    /**
     * 自定义的sql
     */
    private String scopeSql;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联的角色
     */
    private String roleIds;

    /**
     * 是否禁用
     */
    private Integer noUse;

    /**
     * 全局生效，0=否，1=是
     */
    private Integer globalized;
}
