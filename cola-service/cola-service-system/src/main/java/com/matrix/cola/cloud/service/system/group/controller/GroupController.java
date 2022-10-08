package com.matrix.cola.cloud.service.system.group.controller;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.controller.AbstractEntityController;
import com.matrix.cola.cloud.service.system.group.service.GroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织机构管理
 *
 * @author : cui_feng
 * @since : 2022-05-30 17:25
 */
@RestController
@RequestMapping("/group")
public class GroupController extends AbstractEntityController<GroupEntity, GroupService> {


    public GroupController(GroupService service) {
        super(service);
    }

    @PostMapping("/getGroupTreeByUser")
    public Result getGroupTreeByUser(UserEntity userPO) {
        return service.getGroupTreeByUser(userPO);
    }

    @PostMapping("/getGroupTreeByCurrentUser")
    public Result getGroupTreeByCurrentUser() {
        return service.getGroupTreeByCurrentUser();
    }

    @PostMapping("/getGroupTree")
    public Result getGroupTree(@RequestBody Query<GroupEntity> query) {
        return service.getGroupTree(query);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result add(@RequestBody GroupEntity groupPO) {
        return super.add(groupPO);
    }

    @Override
    @PreAuthorize("hasAuthority('administrator')" + "|| hasAuthority('admin')")
    public Result update(@RequestBody GroupEntity groupPO) {
        return super.update(groupPO);
    }
}
