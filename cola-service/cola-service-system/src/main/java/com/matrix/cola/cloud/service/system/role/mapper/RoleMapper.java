package com.matrix.cola.cloud.service.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import org.apache.ibatis.annotations.Delete;

/**
 * 角色Mapper
 *
 * @author : cui_feng
 * @since : 2022-05-11 10:46
 */
public interface RoleMapper extends BaseMapper<RoleEntity> {

    @Delete("delete from system_role where id=#{id}")
    int deleteRole(Long id);
}
