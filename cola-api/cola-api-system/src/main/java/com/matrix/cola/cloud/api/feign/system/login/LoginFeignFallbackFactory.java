package com.matrix.cola.cloud.api.feign.system.login;

import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务降级
 *
 * @author : cui_feng
 * @since : 2022-09-27 16:40
 */
public class LoginFeignFallbackFactory implements FallbackFactory<LoginServiceFeign> {

    @Override
    public LoginServiceFeign create(Throwable cause) {

        return new LoginServiceFeign() {
            @Override
            public UserEntity getUserByLoginName(String loginName) {
                return new UserEntity();
            }

            @Override
            public List<String> getUserRoleCodeList(Long userid) {
                return new ArrayList<>();
            }
        };
    }
}
