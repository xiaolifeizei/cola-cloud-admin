package com.matrix.cola.cloud.api.entity.system.group;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.matrix.cola.cloud.api.common.ColaConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 组织机构树
 *
 * @author : cui_feng
 * @since : 2022-05-30 15:08
 */
@Data
public class GroupTree extends GroupEntity {

    /**
     * 子节点
     */
    public List<GroupTree> children = new ArrayList<>();

    /**
     * 获取组织机构树
     * @param groupList 组织机构列表
     * @return 组织机构树
     */
    public static List<GroupTree> getGroupTree(List<GroupEntity> groupList) {
        List<GroupTree> groupTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(groupList)) {
            return groupTreeList;
        }

        List<GroupEntity> hasTreeList = new ArrayList<>();

        groupTreeList = getGroupTree(ColaConstant.TREE_ROOT_ID, groupList, hasTreeList);

        GoOn:
        for (GroupEntity group : groupList) {
            for (GroupEntity groupTree : hasTreeList) {
                if (Objects.equals(groupTree.getId(),group.getId())) {
                    continue GoOn;
                }
            }
            GroupTree groupTree = getGroupTree(group);
            groupTree.setChildren(getGroupTree(group.getId(), groupList, hasTreeList));
            groupTreeList.add(groupTree);
        }


        return groupTreeList;
    }

    /**
     * 递归获取组织机构树
     * @param parentId 父节点
     * @param groupList 组织机构列表
     * @param hasTreeList 已经生成树的节点
     * @return 组织机构树
     */
    private static List<GroupTree> getGroupTree(Long parentId, List<GroupEntity> groupList, List<GroupEntity> hasTreeList) {
        List<GroupTree> groupTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(groupList)) {
            return groupTreeList;
        }

        for (GroupEntity group : groupList) {
            if (Objects.equals(group.getParentId(),parentId)) {
                hasTreeList.add(group);
                GroupTree groupTree = getGroupTree(group);
                groupTree.setChildren(getGroupTree(group.getId(), groupList, hasTreeList));
                groupTreeList.add(groupTree);
            }
        }

        return groupTreeList;
    }

    /**
     * 将组织机构对象转换成树对象
     * @param groupEntity 机构对象
     * @return 机构树对象
     */
    private static GroupTree getGroupTree(GroupEntity groupEntity) {
        GroupTree groupTree = new GroupTree();
        BeanUtil.copyProperties(groupEntity,groupTree);
        return groupTree;
    }
}
