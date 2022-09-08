package com.matrix.cola.cloud.service.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.role.RoleUserEntity;
import org.apache.ibatis.annotations.Delete;

/**
 * 角色与用户映射Mapper
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:52
 */
public interface RoleUserMapper extends BaseMapper<RoleUserEntity> {

    @Delete("delete from system_role_user where id=#{id}")
    int deleteRoleUser(Long id);

    @Delete("delete from system_role_user where role_id=#{roleId}")
    int deleteRoleUsersByRoleId(Long roleId);

    @Delete("delete from system_role_user where user_id=#{userId}")
    int deleteRoleUsersByUserId(Long userId);
}
