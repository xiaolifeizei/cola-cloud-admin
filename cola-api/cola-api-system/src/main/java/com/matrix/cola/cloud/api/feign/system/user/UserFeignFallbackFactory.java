package com.matrix.cola.cloud.api.feign.system.user;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级
 *
 * @author : cui_feng
 * @since : 2022-09-27 16:40
 */
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserServiceFeign> {

    @Override
    public UserServiceFeign create(Throwable cause) {
        return new UserServiceFeign() { };
    }
}
