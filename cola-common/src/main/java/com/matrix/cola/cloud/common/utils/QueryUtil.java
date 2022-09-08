package com.matrix.cola.cloud.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.entity.QueryCondition;
import com.matrix.cola.cloud.api.common.entity.QueryOrder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 查询条件工具类
 *
 * @author : cui_feng
 * @since : 2022-05-16 15:05
 */
public class QueryUtil<T extends BaseEntity> {

    private static final String IGNORE = "ignore";

    /**
     * 查询条件
     */
    private final Query<T> query;

    private final HashSet<String> fieldNameSet = new HashSet<>();

    public QueryUtil(Query<T> query) {
        this.query = query;
        if (ObjectUtil.isNotNull(query) && ObjectUtil.isNotNull(query.getData())) {
            Field [] fields = ReflectUtil.getFields(query.getData().getClass());
            if (ObjectUtil.isNotEmpty(fields)) {
                for (Field field : fields) {
                    fieldNameSet.add(field.getName());
                }
            }
        }
    }

    /**
     * 通过Query对象生成QueryWrapper
     * @return QueryWrapper
     */
    public QueryWrapper<T> getWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(query)) {
            return queryWrapper;
        }

        List<QueryCondition> conditionList = query.getConditions();
        if (ObjectUtil.isNotEmpty(conditionList)) {
            processNesting(queryWrapper, conditionList);
        }

        if (ObjectUtil.isNotEmpty(fieldNameSet)) {
            for (String fieldName : fieldNameSet) {
                Object fieldValue = ReflectUtil.getFieldValue(query.getData(), fieldName);
                if (ObjectUtil.isNotEmpty(fieldValue)) {
                    queryWrapper.eq(StrUtil.toUnderlineCase(fieldName),fieldValue);
                }
            }
        }

        getOrderByWrapper(queryWrapper,query.getOrderBy());
        return queryWrapper;
    }

    private void getOrderByWrapper(QueryWrapper<T> queryWrapper, List<QueryOrder> orderByList) {
        if (ObjectUtil.isNull(queryWrapper) || ObjectUtil.isEmpty(orderByList)) {
            return;
        }
        for(QueryOrder order : orderByList) {
            if (StrUtil.isEmpty(order.getName())) {
                continue;
            }
            queryWrapper.orderBy(true,order.isAsc(),order.getName());
        }
    }

    /**
     * 处理嵌套查询，无限递归
     * @param queryWrapper 查询wrapper
     * @param conditionList 查询表达式对象
     */
    private void processNesting(QueryWrapper<T> queryWrapper,List<QueryCondition> conditionList) {
        for (QueryCondition condition : conditionList) {
            if (condition.isAndCondition()) {
                queryWrapper.and(q -> {
                    processNesting(q,condition.getConditions());
                });
            } else if (condition.isOrCondition()) {
                queryWrapper.or(q -> {
                    processNesting(q,condition.getConditions());
                });
            } else {
                getConditionWrapper(queryWrapper,condition);
            }
            if (condition.isOrExp()) {
                queryWrapper.or();
            }
        }
    }

    private void getConditionWrapper(QueryWrapper<T> queryWrapper,QueryCondition condition) {

        if (StrUtil.isEmpty(condition.getName())) {
            return;
        }

        if (ObjectUtil.isNotEmpty(fieldNameSet)) {
            fieldNameSet.remove(condition.getName());
        }

        String keyword = condition.getKeyword();
        if (StrUtil.isEmpty(keyword)) {
           keyword = "eq";
        }

        // 去掉忽略属性
        if (IGNORE.equalsIgnoreCase(keyword)) {
            fieldNameSet.remove(condition.getName());
        }

        String value = condition.getValue();
        // 处理between
        if (!condition.isBetween()) {
            if (ObjectUtil.isNotNull(query.getData()) && StrUtil.isEmpty(value)) {
                Object fieldValue = ReflectUtil.getFieldValue(query.getData(),condition.getName());
                if (ObjectUtil.isNotNull(fieldValue)) {
                    value = fieldValue.toString();
                }
            }
            if (StrUtil.isEmpty(value)) {
                return;
            }
        }

        // 驼峰转下划线
        condition.setName(StrUtil.toUnderlineCase(condition.getName()));

        switch (keyword.trim()) {
            case "eq":
                queryWrapper.eq(condition.getName(),value);break;
            case "ne":
                queryWrapper.ne(condition.getName(),value);break;
            case "gt":
                queryWrapper.gt(condition.getName(),value);break;
            case "ge":
                queryWrapper.ge(condition.getName(),value);break;
            case "lt":
                queryWrapper.lt(condition.getName(),value);break;
            case "le":
                queryWrapper.le(condition.getName(),value);break;
            case "between":
                if (condition.isBetween()) {
                    queryWrapper.between(condition.getName(),condition.getValue1(),condition.getValue2());
                }
                break;
            case "notBetween":
                if (condition.isBetween()){
                    queryWrapper.notBetween(condition.getName(),condition.getValue1(),condition.getValue2());
                }
                break;
            case "like":
                queryWrapper.like(condition.getName(),value);break;
            case "notLike":
                queryWrapper.notLike(condition.getName(),value);break;
            case "likeLeft":
                queryWrapper.likeLeft(condition.getName(),value);break;
            case "likeRight":
                queryWrapper.likeRight(condition.getName(),value);break;
            case "isNull":
                queryWrapper.isNull(condition.getName());break;
            case "isNotNull":
                queryWrapper.isNotNull(condition.getName());break;
            case "in":
                if (value.split(",").length > 1) {
                    queryWrapper.in(condition.getName(),Arrays.asList(value.split(",")));
                } else {
                    queryWrapper.in(condition.getName(),value);
                }
                break;
            case "notIn":
                queryWrapper.notIn(condition.getName(),value);break;
            default:
        }
    }

    /**
     * 获取分页对象
     * @return Page<BasePO>
     */
    public IPage<T> getPage() {
        Page<T> page = new Page<>();
        if(ObjectUtil.isNotNull(query)) {
            page.setSize(query.getPageSize());
            page.setCurrent(query.getCurrentPage());
        }
        return page;
    }
}
