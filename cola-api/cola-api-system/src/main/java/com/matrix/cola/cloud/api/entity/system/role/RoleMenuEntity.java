package com.matrix.cola.cloud.api.entity.system.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import lombok.Data;

/**
 * 角色菜单实体类
 *
 * @author : cui_feng
 * @since : 2022-05-11 17:21
 */
@Data
@TableName("system_role_menu")
public class RoleMenuEntity extends BaseEntity {

    /**
     * id号
     * 默认数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
