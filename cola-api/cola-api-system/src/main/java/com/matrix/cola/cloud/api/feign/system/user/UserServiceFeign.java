package com.matrix.cola.cloud.api.feign.system.user;

import com.matrix.cola.cloud.api.common.feign.BaseColaFeign;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cola-system", contextId = "system-user-client")
public interface UserServiceFeign extends BaseColaFeign<UserEntity> {

}