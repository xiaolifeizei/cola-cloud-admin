package com.matrix.cola.cloud.api.feign.system.login;

import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务降级
 *
 * @author : cui_feng
 * @since : 2022-09-27 16:40
 */
@Component
public class LoginFeignFallbackFactory implements FallbackFactory<LoginServiceFeign> {

    @Override
    public LoginServiceFeign create(Throwable cause) {

        return new LoginServiceFeign() {
            @Override
            public UserEntity getUserByLoginName(String loginName) {
                UserEntity userEntity = new UserEntity();
                userEntity.setLoginName("Service Fallback");
                userEntity.setNoUse(ColaConstant.NO);
                return userEntity;
            }

            @Override
            public List<String> getUserRoleCodeList(Long userid) {
                List<String> roleCodeList = new ArrayList<>();
                roleCodeList.add(ColaConstant.DEFAULT_ROLE_CODE);
                return roleCodeList;
            }
        };
    }
}
