package com.matrix.cola.cloud.api.feign.system.login;

import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户登录Feign客户端
 *
 * @author : cui_feng
 * @since : 2022-09-16 11:19
 */
@FeignClient(value = "cola-system", contextId = "system-login-client", path = "login", fallbackFactory = LoginFeignFallbackFactory.class)
public interface LoginServiceFeign {

    /**
     * 通过用户名获取登录用户
     * @param loginName 用户名
     * @return UserEntity
     */
    @PostMapping("/getUserByLoginName")
    UserEntity getUserByLoginName(@RequestParam String loginName);


    /**
     * 获取分配给该用户的角色编码集合
     * @param userid 用户id
     * @return 授权给该用户的所有权限的编码集合
     */
    @PostMapping("/getUserRoleCodeList")
    List<String> getUserRoleCodeList(@RequestParam Long userid);

}
