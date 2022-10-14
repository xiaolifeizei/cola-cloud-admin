package com.matrix.cola.cloud.api.feign.system.group;

import com.matrix.cola.cloud.api.common.feign.AbstractFeignFallbackFactory;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 机构服务降级
 *
 * @author : cui_feng
 * @since : 2022-10-14 17:27
 */
@Component
@Slf4j
public class GroupFeignFallbackFactory extends AbstractFeignFallbackFactory<GroupEntity> implements GroupServiceFeign, FallbackFactory<GroupServiceFeign> {
    @Override
    public GroupServiceFeign create(Throwable cause) {
        log.error(cause.getMessage());
        cause.printStackTrace();
        return new GroupFeignFallbackFactory();
    }
}
