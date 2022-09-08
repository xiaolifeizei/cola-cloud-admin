package com.matrix.cola.cloud.service.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单管理Mapper
 *
 * @author : cui_feng
 * @since : 2022-05-11 14:29
 */
public interface MenuMapper extends BaseMapper<MenuEntity> {

    /**
     * 获取用户的菜单,只显示不隐藏的
     * @param roleIdList 角色id集合
     * @return 菜单实体类集合
     */
    @Select({
        "<script>",
            "select m.* from system_menu m INNER JOIN system_role_menu rm on m.id = rm.menu_id where rm.role_id in",
            "<foreach collection='roleIdList' item='roleId' open='(' separator=',' close=')'>",
                "#{roleId}",
            "</foreach>",
            " and m.hidden=0 and m.menu_type=0",
            " order by m.orders ",
        "</script>"
    })
    List<MenuEntity> getMenuByRoleIds(List<Long> roleIdList);

    /**
     * 获取用户的按钮
     * @param roleIdList 角色id集合
     * @return 菜单实体类集合
     */
    @Select({
            "<script>",
            "select m.* from system_menu m INNER JOIN system_role_menu rm on m.id = rm.menu_id where rm.role_id in",
            "<foreach collection='roleIdList' item='roleId' open='(' separator=',' close=')'>",
            "#{roleId}",
            "</foreach>",
            "and m.menu_type=1",
            " order by m.orders ",
            "</script>"
    })
    List<MenuEntity> getButtonsByRoleIds(List<Long> roleIdList);

    /**
     *获取所有菜单，用于超级管理员
     * @return 菜单实体类集合
     */
    @Select("select m.* from system_menu m where  m.hidden=0 and m.menu_type=0 order by m.orders")
    List<MenuEntity> getAllMenus();

    /**
     *获取所有按钮，用于超级管理员
     * @return 按钮集合
     */
    @Select("select m.* from system_menu m where m.hidden=0 and m.menu_type=1 order by m.orders")
    List<MenuEntity> getAllButtons();

    /**
     * 物理删除菜单
     * @param id 菜单id
     * @return 删除的条数
     */
    @Delete("delete from system_menu where id=#{id}")
    int deleteMenu(Long id);
}
