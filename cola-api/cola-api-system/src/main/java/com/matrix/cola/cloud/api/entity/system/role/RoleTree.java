package com.matrix.cola.cloud.api.entity.system.role;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.matrix.cola.cloud.api.common.ColaConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色树
 *
 * @author : cui_feng
 * @since : 2022-05-30 13:31
 */
@Data
public class RoleTree extends RoleEntityWrapper {

    /**
     * 子节点
     */
    private List<RoleTree> children = new ArrayList<>();

    /**
     * 获取角色树
     * @param roleList 角色列表
     * @return 角色树
     */
    public static List<RoleTree> getRoleTree(List<RoleEntityWrapper> roleList) {
        List<RoleTree> roleTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(roleList)) {
            return roleTreeList;
        }

        List<RoleEntityWrapper> hasTreeList = new ArrayList<>();

        roleTreeList = getRoleTree(ColaConstant.TREE_ROOT_ID, roleList, hasTreeList);

        GoOn:
        for (RoleEntityWrapper role : roleList) {
            for (RoleEntityWrapper roleTree : hasTreeList) {
                if (Objects.equals(role.getId(), roleTree.getId())) {
                    continue GoOn;
                }
            }
            RoleTree roleTree = getRoleTree(role);
            roleTree.setChildren(getRoleTree(role.getId(), roleList, hasTreeList));
            roleTreeList.add(roleTree);
        }

        return roleTreeList;
    }

    /**
     * 递归获取角色树
     * @param parentId 父节点
     * @param roleList 角色列表
     * @param hasTreeList 已生成树的角色列表
     * @return 角色树
     */
    private static List<RoleTree> getRoleTree(Long parentId, List<RoleEntityWrapper> roleList, List<RoleEntityWrapper> hasTreeList) {
        List<RoleTree> roleTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(roleList)) {
            return roleTreeList;
        }

        for (RoleEntityWrapper role : roleList) {
            if (Objects.equals(role.getParentId(), parentId)) {
                hasTreeList.add(role);
                RoleTree roleTree = getRoleTree(role);
                roleTree.setChildren(getRoleTree(role.getId(), roleList, hasTreeList));
                roleTreeList.add(roleTree);
            }
        }

        return roleTreeList;
    }

    /**
     * 将角色对象转换成角色树对象
     * @param roleWrapper 角色对象
     * @return 角色树
     */
    private static RoleTree getRoleTree(RoleEntityWrapper roleWrapper) {
        RoleTree roleTree = new RoleTree();
        BeanUtil.copyProperties(roleWrapper,roleTree);
        return roleTree;
    }
}
