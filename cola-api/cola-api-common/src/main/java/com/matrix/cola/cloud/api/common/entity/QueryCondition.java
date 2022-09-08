package com.matrix.cola.cloud.api.common.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件对象
 *
 * @author : cui_feng
 * @since : 2022-05-17 10:04
 */
@Data
public class QueryCondition implements Serializable {
    /**
     * 字段名
     */
    private String name;

    /**
     * 查询关键字,如：eq、ne、like,between等,默认为eq
     */
    private String keyword;

    /**
     * 字段值
     */
    private String value;

    /**
     * 用于between和notBetween查询，如: between value1 and value2
     */
    private String value1;

    /**
     * 用于between和notBetween查询，如: between value1 and value2
     */
    private String value2;

    /**
     * 连接表达式，只能为and或or,默认为and
     */
    private String exp = "and";

    /**
     * 连接表达式，只作用于嵌套查询，只能为and或or,默认为and
     */
    private List<QueryCondition> conditions;

    /**
     * 判断exp是否为and
     * @return boolean
     */
    public boolean isAndExp() {
        return "and".equalsIgnoreCase(exp);
    }
    /**
     * 判断exp是否为or
     * @return boolean
     */
    public boolean isOrExp() {
        return "or".equalsIgnoreCase(exp);
    }

    /**
     * 判断表达式是否是and嵌套
     * @return
     */
    public boolean isAndCondition() {
        return "and".equalsIgnoreCase(StrUtil.emptyToDefault(keyword,"and")) && ObjectUtil.isNotEmpty(conditions);
    }

    /**
     * 判断表达式是否是or嵌套
     * @return
     */
    public boolean isOrCondition() {
        return "or".equalsIgnoreCase(keyword) && ObjectUtil.isNotEmpty(conditions);
    }

    /**
     * 是否为between
     * @return boolean
     */
    public boolean isBetween() {
        return keyword!=null &&
                keyword.contains("between") &&
                (StringUtils.isNotEmpty(value1) && StringUtils.isNotEmpty(value2));
    }
}
