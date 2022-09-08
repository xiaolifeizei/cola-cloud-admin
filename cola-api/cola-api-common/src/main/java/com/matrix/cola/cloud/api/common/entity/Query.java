package com.matrix.cola.cloud.api.common.entity;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件封装
 *
 * @author : cui_feng
 * @since : 2022-05-16 13:49
 */
@Data
public class Query<T extends BaseEntity> implements Serializable {

    /**
     * 查询条件
     */
    private List<QueryCondition> conditions = new ArrayList<>();

    /**
     * 查询值
     */
    private T data;

    /**
     * 当前页
     */
    private Integer currentPage = 1;

    /**
     * 每页记录条数，默认20条
     */
    private Integer pageSize = 20;

    /**
     * 排序条件
     */
    private List<QueryOrder> orderBy = new ArrayList<>();

    public Query<T> eq(String column, String value, String exp){
        return condition(column,"eq",value,exp);
    }

    public Query<T> eq(String column, String value){
        return eq(column,value,null);
    }

    public Query<T> ne(String column, String value, String exp){
        return condition(column,"ne",value,exp);
    }

    public Query<T> ne(String column, String value){
        return ne(column,value,null);
    }

    public Query<T> gt(String column, String value, String exp){
        return condition(column,"gt",value,exp);
    }

    public Query<T> gt(String column, String value){
        return gt(column,value,null);
    }

    public Query<T> ge(String column, String value, String exp){
        return condition(column,"ge",value,exp);
    }

    public Query<T> ge(String column, String value){
        return ge(column,value,null);
    }

    public Query<T> lt(String column, String value, String exp){
        return condition(column,"lt",value,exp);
    }

    public Query<T> lt(String column, String value){
        return lt(column,value,null);
    }

    public Query<T> le(String column, String value, String exp){
        return condition(column,"le",value,exp);
    }

    public Query<T> le(String column, String value){
        return le(column,value,null);
    }

    public Query<T> between(String column, String value1, String value2, String exp){
        return addCondition(column,"between",null,value1,value2,exp);
    }

    public Query<T> between(String column, String value1, String value2){
        return between(column,value1,value2,null);
    }

    public Query<T> notBetween(String column, String value1, String value2, String exp){
        return addCondition(column,"notBetween",null,value1,value2,exp);
    }

    public Query<T> notBetween(String column, String value1, String value2){
        return notBetween(column,value1,value2,null);
    }

    public Query<T> like(String column, String value, String exp){
        return condition(column,"like",value,exp);
    }

    public Query<T> like(String column, String value){
        return like(column,value,null);
    }

    public Query<T> notLike(String column, String value, String exp){
        return condition(column,"notLike",value,exp);
    }

    public Query<T> notLike(String column, String value){
        return notLike(column,value,null);
    }

    public Query<T> likeLeft(String column, String value, String exp){
        return condition(column,"likeLeft",value,exp);
    }

    public Query<T> likeLeft(String column, String value){
        return likeLeft(column,value,null);
    }

    public Query<T> likeRight(String column, String value, String exp){
        return condition(column,"likeRight",value,exp);
    }

    public Query<T> likeRight(String column, String value){
        return likeRight(column,value,null);
    }

    public Query<T> isNull(String column, String value, String exp){
        return condition(column,"isNull",value,exp);
    }

    public Query<T> isNull(String column, String value){
        return isNull(column,value,null);
    }

    public Query<T> condition(String column, String keyword, String value, String exp) {
        return addCondition(column,keyword,value,null,null,exp);
    }

    public Query<T> isNotNull(String column, String value, String exp){
        return condition(column,"isNotNull",value,exp);
    }

    public Query<T> isNotNull(String column, String value){
        return isNotNull(column,value,null);
    }

    public Query<T> in(String column, String value, String exp){
        return condition(column,"in",value,exp);
    }

    public Query<T> in(String column, String value){
        return in(column,value,null);
    }

    public Query<T> in(String column, List list, String exp){
        return condition(column,"in",listToString(list),exp);
    }

    public Query<T> in(String column, List list){
        return in(column,list,null);
    }

    public Query<T> notIn(String column, String value, String exp){
        return condition(column,"notIn",value,exp);
    }

    public Query<T> notIn(String column, String value){
        return notIn(column,value,null);
    }

    public Query<T> notIn(String column, List list, String exp){
        return condition(column,"notIn",listToString(list),exp);
    }

    public Query<T> notIn(String column, List list){
        return notIn(column,list,null);
    }

    public Query<T> orderBy(String column, boolean isAsc) {
        if (ObjectUtils.isEmpty(column)) {
            return this;
        }
        QueryOrder queryOrder = new QueryOrder();
        queryOrder.setName(column);
        queryOrder.setAsc(isAsc);
        orderBy.add(queryOrder);
        return this;
    }

    public Query<T> orderBy(String column) {
        return orderBy(column, true);
    }

    private Query<T> addCondition(String column, String keyword, String value, String value1, String value2, String exp) {
        if (ObjectUtils.isEmpty(column) || ObjectUtils.isEmpty(keyword)) {
            return this;
        }
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setName(column);
        queryCondition.setKeyword(keyword);
        queryCondition.setValue(value);
        queryCondition.setValue1(value1);
        queryCondition.setValue2(value2);
        queryCondition.setExp(ObjectUtils.isEmpty(exp) || !"or".equalsIgnoreCase(exp) ? "and" : "or");
        conditions.add(queryCondition);
        return this;
    }

    private String listToString(List list){
        if (ObjectUtils.isEmpty(list)){
            return null;
        }
        StringBuilder value = new StringBuilder();
        for (int i=0; i<list.size(); i++){
            if (i==0){
                value.append(list.get(i));
            } else {
                value.append(",").append(list.get(i));
            }
        }
        return value.toString();
    }
}
