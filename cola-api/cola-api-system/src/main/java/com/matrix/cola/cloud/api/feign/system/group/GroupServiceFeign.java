package com.matrix.cola.cloud.api.feign.system.group;

import com.matrix.cola.cloud.api.common.feign.BaseColaFeign;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cola-system", contextId = "system-group-client")
public interface GroupServiceFeign extends BaseColaFeign<GroupEntity> {

}
