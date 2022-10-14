package com.matrix.cola.cloud.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.matrix.cola.cloud.common.interceptor.ColaBlockAttackInnerInterceptor;
import com.matrix.cola.cloud.common.interceptor.DataScopeInterceptor;
import com.matrix.cola.cloud.common.interceptor.DataScopeQueryProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置类
 *
 * @author : cui_feng
 * @since : 2022-04-11 15:12
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
public class MybatisPlusConfig {


    /**
     * 加载插件
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限插件
        interceptor.addInnerInterceptor(getDataScopeInterceptor());
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(getBlockAttackInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public DataScopeInterceptor getDataScopeInterceptor() {
        return new DataScopeInterceptor();
    }

    @Bean
    public DataScopeQueryProcessor getDataScopeProcessor() {
        return new DataScopeQueryProcessor();
    }

    @Bean
    public BlockAttackInnerInterceptor getBlockAttackInnerInterceptor() {
        return new ColaBlockAttackInnerInterceptor();
    }
}
