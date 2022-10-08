package com.matrix.cola.cloud.service.system.datascope.initializer;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.service.system.datascope.service.DataScopeService;
import com.matrix.cola.cloud.service.system.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限初始化
 *
 * @author : cui_feng
 * @since : 2022-06-08 14:54
 */
@Component
@Order(1)
public class DataScopeInitializer implements ApplicationRunner {

    CacheProxy cacheProxy;

    DataScopeService dataScopeService;

    GroupService groupService;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setDataScopeService(DataScopeService dataScopeService) {
        this.dataScopeService = dataScopeService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 加载数据权限到缓存
        cacheProxy.put(ColaCacheName.DATA_SCOPE_LIST,ColaCacheName.DATA_SCOPE_LIST.cacheName(), dataScopeService.getDataScopeMap());

        // 加载组织机构到缓存
        List<GroupEntity> groupList = groupService.getList(new Query<>());
        if (ObjectUtil.isNotEmpty(groupList)) {
            for (GroupEntity group : groupList) {
                cacheProxy.put(ColaCacheName.GROUP_ENTITY, group.getId().toString(), group);
            }
        }
    }
}
