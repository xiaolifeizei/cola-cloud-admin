package com.matrix.cola.cloud.service.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.role.RoleMenuEntity;
import org.apache.ibatis.annotations.Delete;

/**
 * 角色菜单Mapper
 *
 * @author : cui_feng
 * @since : 2022-05-11 17:22
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenuEntity> {

    /**
     * 删除角色菜单，物理删除
     * @param id id号
     * @return 删除的条数
     */
    @Delete("delete from system_role_menu where id=#{id}")
    int deleteRoleMenu(Long id);

    /**
     * 通过角色id删除角色菜单，物理删除
     * @param roleId 角色id
     * @return 删除的条数
     */
    @Delete("delete from system_role_menu where role_id=#{roleId}")
    int deleteRoleMenusByRoleId(Long roleId);
}
