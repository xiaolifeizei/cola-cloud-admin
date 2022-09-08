package com.matrix.cola.cloud.api.entity.system.menu;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * vue中的菜单树
 *
 * @author : cui_feng
 * @since : 2022-05-12 09:16
 */
@Data
public class VueMenu implements Serializable {

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 组件
     */
    private String component;

    /**
     * 子菜单
     */
    private List<VueMenu> children;


    /**
     * 递归生成
     * @param parentId 父id
     * @param menuList 菜单列表
     * @return 生成的树
     */
    public static List<VueMenu> getVueTree(Long parentId,List<MenuEntity> menuList) {
        List<VueMenu> treeList = new LinkedList<>();
        if(ObjectUtils.isEmpty(menuList)) {
            return treeList;
        }

        for (MenuEntity menuEntity : menuList) {
            if (Objects.equals(parentId,menuEntity.getParentId())) {
                VueMenu vueMenu = new VueMenu();
                vueMenu.setId(menuEntity.getId());
                vueMenu.setName(menuEntity.getCode());
                vueMenu.setTitle(menuEntity.getName());
                vueMenu.setPath(menuEntity.getUrl());
                vueMenu.setIcon(menuEntity.getIcon());
                vueMenu.setComponent(menuEntity.getComponent());
                vueMenu.setChildren(getVueTree(menuEntity.getId(), menuList));
                treeList.add(vueMenu);
            }
        }
        return treeList;
    }
}
