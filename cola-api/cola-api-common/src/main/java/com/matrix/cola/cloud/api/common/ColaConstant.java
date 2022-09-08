package com.matrix.cola.cloud.api.common;

/**
 * 常量定义
 *
 * @author : cui_feng
 * @since : 2022-06-02 23:51
 */
public class ColaConstant {

    /**
     * 默认的用户角色编码
     */
    public static final String DEFAULT_ROLE_CODE = "guest";

    /**
     * 树的根节点
     */
    public static final Long TREE_ROOT_ID = 0L;

    /**
     * 缓存中的机构是否缓存全名，若为false则缓存简称
     */
    public static final boolean IS_FULL_GROUP_NAME = true;

    /**
     * 超级管理员id
     */
    public static final Long ADMINISTRATOR_ID = 1L;

    /**
     * 全部数据
     */
    public static final int DATA_SCOPE_TYPE_ALL = 0;

    /**
     * 本机构数据
     */
    public static final int DATA_SCOPE_TYPE_LOCAL = 1;

    /**
     * 本机构数据及下级机构
     */
    public static final int DATA_SCOPE_TYPE_LOCAL_NEXT = 2;

    /**
     * 本人数据
     */
    public static final int DATA_SCOPE_TYPE_SELF = 3;

    /**
     * 自定义
     */
    public static final int DATA_SCOPE_TYPE_CUSTOM = 4;

    /**
     * 是
     */
    public static final int YES = 1;

    /**
     * 否
     */
    public static final int NO = 0;

}
