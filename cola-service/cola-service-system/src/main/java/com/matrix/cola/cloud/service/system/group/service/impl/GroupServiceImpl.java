package com.matrix.cola.cloud.service.system.group.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import com.matrix.cola.cloud.api.entity.system.group.GroupTree;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.service.AbstractEntityService;
import com.matrix.cola.cloud.common.utils.WebUtil;
import com.matrix.cola.cloud.service.system.group.mapper.GroupMapper;
import com.matrix.cola.cloud.service.system.group.service.GroupService;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 组织机构实体服务接口实现类
 *
 * @author : cui_feng
 * @since : 2022-05-30 15:06
 */
@Service
public class GroupServiceImpl extends AbstractEntityService<GroupEntity, GroupMapper> implements GroupService {


    @Autowired
    UserService userService;



    @Override
    public Result getGroupTreeByUser(UserEntity userEntity) {
        if (ObjectUtil.isNull(userEntity) || ObjectUtil.isNull(userEntity.getId())) {
            return Result.list(Collections.emptyList());
        }
        if (ObjectUtil.isEmpty(userEntity.getGroupId())) {
            userEntity = userService.getOne(userEntity.getId());
            if (ObjectUtil.isNull(userEntity) || StrUtil.isEmpty(userEntity.getGroupId())) {
                return Result.list(Collections.emptyList());
            }
        }
        LambdaQueryWrapper<GroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!WebUtil.isAdministrator()) {
            queryWrapper.in(GroupEntity::getId, Arrays.asList(userEntity.getGroupId().split(",")));
            List<GroupEntity> groupList = getList(queryWrapper);
            if (ObjectUtil.isEmpty(groupList)) {
                return Result.list(Collections.emptyList());
            }
            for (GroupEntity group : groupList) {
                if (StrUtil.isEmpty(group.getAncestors())){
                    continue;
                }
                queryWrapper.or();
                queryWrapper.in(GroupEntity::getId, Arrays.asList(group.getAncestors().split(",")));
            }
        }

        return Result.list(GroupTree.getGroupTree(getList(queryWrapper)));
    }

    @Override
    public Result getGroupTreeByCurrentUser() {
        UserEntity userPO = WebUtil.getUser();
        if (ObjectUtil.isNull(userPO) || ObjectUtil.isEmpty(userPO.getGroupId())) {
            return Result.list(Collections.emptyList());
        }
        return getGroupTreeByUser(userPO);
    }

    @Override
    public Result getGroupTree(Query<GroupEntity> query) {
        return Result.list(GroupTree.getGroupTree(getList(query)));
    }

    @Override
    protected Result validate(GroupEntity entity) {
        if (ObjectUtil.isEmpty(entity.getName())) {
            return Result.err("操作失败，机构名称不能为空");
        }
        if (ObjectUtil.isEmpty(entity.getShortName())) {
            return Result.err("操作失败，机构简称不能为空");
        }
        LambdaQueryWrapper<GroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupEntity::getId, entity.getParentId());
        if (!Objects.equals(ColaConstant.TREE_ROOT_ID, entity.getParentId()) && ObjectUtil.isNull(getOne(queryWrapper))) {
            return Result.err("操作失败，没有找到父节点");
        }
        queryWrapper.clear();
        queryWrapper.eq(GroupEntity::getName, entity.getName()).or().eq(GroupEntity::getShortName,entity.getShortName());
        GroupEntity groupEntity = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(groupEntity) && !Objects.equals(entity.getId(), groupEntity.getId())) {
            return Result.err("操作失败，机构名称或机构简称已存在");
        }

        return Result.ok();
    }

    @Override
    protected Result beforeUpdate(GroupEntity entity) {
        LambdaQueryWrapper<GroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupEntity::getId, entity.getParentId());
        GroupEntity parent = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(parent)) {
            StringBuilder parent_ancestors = new StringBuilder(StrUtil.emptyToDefault(parent.getAncestors(), ""));
            if (StrUtil.isNotEmpty(parent_ancestors.toString())) {
                parent_ancestors.append(",");
            }
            if (StrUtil.isNotEmpty(entity.getAncestors())) {
                List<String> groupIdList = Arrays.asList(entity.getAncestors().split(","));
                List<String> parent_groupIdList = Arrays.asList(parent_ancestors.toString().split(","));
                if (ObjectUtil.isNotEmpty(groupIdList)) {
                    for (String groupId : groupIdList) {
                        if (!parent_groupIdList.contains(groupId)) {
                            parent_ancestors.append(groupId).append(",");
                        }
                    }
                    if (StrUtil.isNotEmpty(parent_ancestors) && parent_ancestors.substring(parent_ancestors.length()-1, parent_ancestors.length()).equals(",")) {
                        parent.setAncestors(parent_ancestors.substring(0,parent_ancestors.length()-1));
                        modify(parent);
                    }

                }
            }
        }
        // 清除缓存
        cacheProxy.evict(ColaCacheName.GROUP_ENTITY, entity.getId().toString());
        return Result.ok();
    }

    @Override
    protected Result afterAdd(GroupEntity entity) {
        entity.setAncestors(entity.getId().toString());
        return modify(entity);
    }

}
