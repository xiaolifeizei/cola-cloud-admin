package com.matrix.cola.cloud.api.feign.system.user;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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

        return new UserServiceFeign() {

            @Override
            public UserEntity getOne(Long id) {
                return null;
            }

            @Override
            public UserEntity getOne(UserEntity entity) {
                return null;
            }

            @Override
            public UserEntity getOne(Query<UserEntity> query) {
                return null;
            }

            @Override
            public List<UserEntity> getEntityList(Query<UserEntity> query) {
                return null;
            }

            @Override
            public Result add(UserEntity entity) {
                return null;
            }

            @Override
            public Result update(UserEntity entity) {
                return null;
            }

            @Override
            public Result delete(UserEntity entity) {
                return null;
            }
        };
    }
}
