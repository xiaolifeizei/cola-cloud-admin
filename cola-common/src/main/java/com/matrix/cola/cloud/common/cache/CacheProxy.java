package com.matrix.cola.cloud.common.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.matrix.cola.cloud.api.common.service.CacheService;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.common.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.concurrent.Callable;

/**
 * 缓存代理工具类
 *
 * @author : cui_feng
 * @since : 2022-04-13 08:44
 */
@AllArgsConstructor
public class CacheProxy implements CacheService {

    @Getter
    private final RedisUtil redisUtil;

    /**
     * 创缓存管理器
     */
    private volatile static CacheManager cacheManager = null;

    /**
     * 获取缓存，
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @return 缓存管理器对象，{@link Cache}接口的实现类
     */
    public Cache getCache(ColaCacheName cacheName){
        // 单例DCL
        if(null == cacheManager){
            synchronized (CacheProxy.class){
                if(null == cacheManager){
                     cacheManager = SpringUtil.getBean(CacheManager.class);
                }
            }
        }
        return cacheManager.getCache(cacheName.cacheName());
    }

    @Override
    public void put(ColaCacheName cacheName, String key, Object value) {
        if (StrUtil.isEmpty(key) || ObjectUtil.isEmpty(value)){
            return;
        }
        Cache cache = getCache(cacheName);
        cache.put(key, value);
    }

    @Override
    public <T> T getObject(ColaCacheName cacheName, String key) {
        if (StrUtil.hasEmpty(cacheName.cacheName(),key)) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = getCache(cacheName).get(key);
        return valueWrapper == null ? null : (T) valueWrapper.get();
    }

    @Override
    public <T> T getObjectFromLoader(ColaCacheName cacheName, String key, Callable<T> valueLoader) {
        if (ObjectUtil.isNull(valueLoader)) {
            return null;
        }
        if (StrUtil.hasEmpty(cacheName.cacheName(),key)) {
            return null;
        }

        Object value = getObject(cacheName,key);
        if (ObjectUtil.isNull(value)) {
            // 分布式锁
            RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
            String lockKey = cacheName.cacheName() + '_' + key;
            for (;;) {
                boolean lock = redisUtil.lock(lockKey);
                if (lock) {
                    try {
                        value = getObject(cacheName,key);
                        // Double Check
                        if (ObjectUtil.isNull(value)) {
                            return getCache(cacheName).get(key,valueLoader);
                        }
                    } finally {
                        redisUtil.unLock(lockKey);
                    }
                } else {
                    try {
                        // 自旋
                        Thread.sleep(200);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        }
        return (T)value;
    }

    @Override
    public <T> T getObjectFromClass(ColaCacheName cacheName, String key, Class<T> t) {
        if (ObjectUtil.isNull(t)) {
            return null;
        }
        if (StrUtil.hasEmpty(cacheName.cacheName(),key)) {
            return null;
        }
        return getCache(cacheName).get(key,t);
    }

    @Override
    public void evict(ColaCacheName cacheName, String key) {
        if (StrUtil.hasEmpty(cacheName.cacheName(),key)) {
            return;
        }
        getCache(cacheName).evict(key);
    }

    @Override
    public void clear(ColaCacheName cacheName) {
        if (!StrUtil.isEmpty(cacheName.cacheName())) {
            getCache(cacheName).clear();
        }
    }

    @Override
    public boolean put(ColaCacheName cacheName, String key, Object value, long time) {
        return redisUtil.set(cacheName.cacheName() + "::" + key, value, time);
    }
}
