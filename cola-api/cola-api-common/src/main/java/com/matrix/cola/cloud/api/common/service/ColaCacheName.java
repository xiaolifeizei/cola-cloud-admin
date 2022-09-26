package com.matrix.cola.cloud.api.common.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import java.io.Serializable;

/**
 * 缓存名称，为防止输入错误，缓存统一使用该接口定义的类型
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public enum ColaCacheName implements Serializable {

    /**
     * 默认值为空
     */
    DEFAULT(""),

    /**
     * dubbo应用程序名
     */
    DUBBO_APPLICATION_NAME("dubbo_application_name"),

    /**
     * 客户
     */
    CUSTOMER("customer"),

    /**
     * 客户分类
     */
    CUSTOMER_CATEGORY("customer_category"),

    /**
     * 客户分类名称
     */
    CUSTOMER_CATEGORY_NAME("customer_category_name"),

    /**
     * 物资
     */
    GOODS("goods"),

    /**
     * 物资分类
     */
    GOODS_CATEGORY("goods_category"),

    /**
     * 物资分类名称
     */
    GOODS_CATEGORY_NAME("goods_category_name"),

    /**
     * 规格
     */
    GOODS_SPEC("goods_spec"),

    /**
     * 当前登陆用户的角色编码
     */
    USER_ROLE_CODES("role_codes"),

    /**
     * 当前登陆用户的角色ID
     */
    USER_ROLE_IDS("role_ids"),

    /**
     * 角色名称
     */
    ROLE_NAME("role_name"),

    /**
     * 数据权限列表
     */
    DATA_SCOPE_LIST("data_scope_list"),

    /**
     * 当前登陆用户的菜单
     */
    USER_MENUS("user_menus"),

    /**
     * 组织机构名称
     */
    GROUP_ENTITY("group_entity"),

    /**
     * 用户姓名
     */
    USER_NAME("user_name"),

    /**
     * OAuth2授权码
     */
    OAUTH2_AUTHORIZATION_CODE("oauth2_authorization_code"),

    /**
     * 用户授权ID
     */
    OAUTH2_APPROVE_ID("oauth2_approve_id"),

    /**
     * 过磅Service
     */
    WEIGH_PROCESSOR("weigh_processor");

    /**
     * 缓存名称
     */
    private final String CACHE_NAME;

    /**
     * 构造方法
     * @param cacheName 缓存名称
     */
    ColaCacheName(String cacheName) {
        this.CACHE_NAME = cacheName;
    }

    /**
     * 获取缓存名
     * @return 缓存名称
     */
    public String cacheName() {
        return this.CACHE_NAME;
    }

    public static ColaCacheName getByName(String name){
        if (ObjectUtils.isEmpty(name)) {
            return DEFAULT;
        }
        for(ColaCacheName cacheName : values()){
            if(cacheName.cacheName().equals(name)){
                return cacheName;
            }
        }
        return DEFAULT;
    }
}
