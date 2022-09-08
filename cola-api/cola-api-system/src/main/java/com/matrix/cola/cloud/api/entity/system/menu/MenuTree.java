package com.matrix.cola.cloud.api.entity.system.menu;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.matrix.cola.cloud.api.common.ColaConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 菜单管理树
 *
 * @author : cui_feng
 * @since : 2022-05-26 14:05
 */
@Data
public class MenuTree extends MenuEntity {

    /**
     * 子节点
     */
    private List<MenuTree> children = new ArrayList<>();


    /**
     * 生成菜单树
     * @param menuList 菜单列表
     * @return 菜单树
     */
    public static List<MenuTree> getMenuTree(List<MenuEntity> menuList) {
        List<MenuTree>  menuTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(menuList)) {
            return menuTreeList;
        }

        // 保存已经生成树节点的菜单
        List<MenuEntity> hasTreeList = new ArrayList<>();

        // 取根节点的
        menuTreeList = getTree(ColaConstant.TREE_ROOT_ID, menuList, hasTreeList);

        // 获取其他非根节点树
        GoOn:
        for (MenuEntity menuPO : menuList) {
            for (MenuEntity menuTree : hasTreeList) {
                if (Objects.equals(menuPO.getId(),menuTree.getId())) {
                    continue GoOn;
                }
            }
            MenuTree menuTree = getMenuTree(menuPO);
            menuTree.setChildren(getTree(menuPO.getId(), menuList, hasTreeList));
            menuTreeList.add(menuTree);
        }

        return menuTreeList;
    }

    /**
     * 递归生成菜单树
     * @param parentId 父节点id
     * @param menuList 菜单列表
     * @return 菜单树
     */
    private static List<MenuTree> getTree(Long parentId, List<MenuEntity> menuList, List<MenuEntity> hasTreeList) {
        List<MenuTree> menuTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(menuList)) {
            return menuTreeList;
        }

        for (MenuEntity menu : menuList) {
            if (Objects.equals(menu.getParentId(), parentId)) {
                hasTreeList.add(menu);
                MenuTree menuTree = getMenuTree(menu);
                menuTree.setChildren(getTree(menu.getId(), menuList, hasTreeList));
                menuTreeList.add(menuTree);
            }
        }

        return menuTreeList;
    }

    /**
     * 将菜单实体对象转换成菜单树对象
     * @param menuEntity
     * @return
     */
    private static MenuTree getMenuTree(MenuEntity menuEntity) {
        MenuTree menuTree = new MenuTree();
        BeanUtil.copyProperties(menuEntity,menuTree);
        return menuTree;
    }
}
