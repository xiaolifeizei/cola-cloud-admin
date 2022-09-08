package com.matrix.cola.cloud.service.system.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntityWrapper;
import com.matrix.cola.cloud.common.service.AbstractColaEntityWrapperService;
import com.matrix.cola.cloud.common.utils.QueryUtil;
import com.matrix.cola.cloud.common.utils.WebUtil;
import com.matrix.cola.cloud.service.system.group.service.GroupService;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import com.matrix.cola.cloud.service.system.user.service.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户实体包装服务接口实现类
 *
 * @author : cui_feng
 * @since : 2022-06-02 11:58
 */
@Service
public class UserWrapperServiceImpl extends AbstractColaEntityWrapperService<UserEntity, UserEntityWrapper, UserService> implements UserWrapper {


    @Autowired
    GroupService groupService;

    @Override
    public Result getCurrentUsersPage(Query<UserEntity> query) {

        if (ObjectUtil.isNull(query)) {
            return Result.err("查询失败，没有获取到参数");
        }

        QueryUtil<UserEntity> queryUtil = new QueryUtil<>(query);
        QueryWrapper<UserEntity> queryWrapper = queryUtil.getWrapper();

        if (ObjectUtil.isNotNull(query.getData()) && StrUtil.isNotEmpty(query.getData().getGroupId())) {
            String groupId = query.getData().getGroupId();
            queryWrapper.and(condition -> {
                condition.or(cd -> {
                    cd.eq("group_id",groupId)
                            .or()
                            .likeRight("group_id", groupId + ",")
                            .or()
                            .likeLeft("group_id", "," + groupId)
                            .or()
                            .like("group_id",groupId);
                });
            });
        }

        // 超管跳过
        if (!WebUtil.isAdministrator()) {
            UserEntity userEntity = WebUtil.getUser();
            if (ObjectUtil.isNull(userEntity) || StrUtil.isEmpty(userEntity.getGroupId())) {
                return Result.err("查询失败，您不属于任何机构，不能查询");
            }
            String [] groupIds = userEntity.getGroupId().split(",");
            queryWrapper.and(condition -> {
                for (String groupId : groupIds) {
                    if (!NumberUtil.isLong(groupId)) {
                        continue;
                    }
                    GroupEntity groupPO = cacheProxy.getObjectFromLoader(ColaCacheName.GROUP_ENTITY, groupId, () -> {
                        LambdaQueryWrapper<GroupEntity> q = new LambdaQueryWrapper<>();
                        q.eq(GroupEntity::getId, groupId);
                        return groupService.getOne(q);
                    });
                    if (ObjectUtil.isEmpty(groupPO)) {
                        continue;
                    }
                    condition.or(cd -> {
                        cd.eq("group_id",groupId)
                                .or()
                                .likeRight("group_id", groupId + ",")
                                .or()
                                .likeLeft("group_id", "," + groupId)
                                .or()
                                .like("group_id",groupId);
                    });
                }
            });

        }
        return Result.page(entityPageWrapper(getService().getPage(queryUtil.getPage(),queryWrapper)));
    }
}
