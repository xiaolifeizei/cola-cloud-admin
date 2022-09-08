package com.matrix.cola.cloud.api.common.service;

import java.util.concurrent.Callable;


/**
 * 缓存接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface CacheService extends BaseService {

    /**
     * 向缓存中添加对象
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @param key 缓存key值
     * @param value 缓存对象
     */
    void put(ColaCacheName cacheName, String key, Object value);

    /**
     * 从缓存中获取一个对象
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @param key 缓存key值
     * @return 缓存对象
     */
    <T> T getObject(ColaCacheName cacheName, String key);


    /**
     * 从缓存中获取指定对象，如果不存在则调用valueloader回调，并将value添加到缓存中
     * 此方法主要用于兼容Redis
     *
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @param key 缓存key值
     * @param valueLoader 值加载器，一个 {@link Callable}接口，值不存在时执行，执行后将对象加入到缓存中
     * @param <T> 缓存对象泛型
     * @return 缓存对象
     */
    <T> T getObjectFromLoader(ColaCacheName cacheName, String key, Callable<T> valueLoader);

    /**
     * 从缓存中获取指定的对象
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @param key 缓存key值
     * @param t 缓存对象的class
     * @param <T> 缓存对象泛型
     * @return 缓存对象
     */
    <T> T getObjectFromClass(ColaCacheName cacheName, String key, Class<T> t);

    /**
     * 从指定的缓存中删除一个指定的缓存
     * @param cacheName 缓存名 {@link ColaCacheName}
     * @param key 缓存key值
     */
    void evict(ColaCacheName cacheName, String key);

    /**
     * 清空指定的缓存
     * @param cacheName 缓存名 {@link ColaCacheName}
     */
    void clear(ColaCacheName cacheName);
}
