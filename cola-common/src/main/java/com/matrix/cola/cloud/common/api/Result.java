package com.matrix.cola.cloud.common.api;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.*;

/**
 * 通用返回类型
 *
 * @author : cui_feng
 * @since : 2022-04-07 14:41
 */
public class Result implements Serializable {

    public static Result ok(Map<String,Object> obj) {
        return new Result(obj);
    }

    public static Result ok(String msg) {
        return new Result().success(msg);
    }

    public static Result ok() {
        return new Result();
    }

    public static Result err(String msg) {
        return new Result().error(msg);
    }

    public static Result err(Integer code,String msg) {
        return new Result(code,msg);
    }

    public static Result list(List list) {return ok().data("list",list);}

    @JsonIgnore
    public List getList() {
        return ObjectUtil.isNull(getData().get("list")) ? null : getData().get("list") instanceof ArrayList ? (List)getData().get("list") : null;
    }

    @JsonIgnore
    public IPage getPage() {
        return ObjectUtil.isNull(getData().get("page")) ? null : getData().get("page") instanceof IPage ? (IPage) getData().get("page") : null;
    }

    @JsonIgnore
    public Map getMap() {
        return ObjectUtil.isNull(getData().get("map")) ? null : getData().get("map") instanceof Map ? (Map) getData().get("map") : null;
    }

    @JsonIgnore
    public Object getObject() {
        return getData().get("obj");
    }

    public static Result map(Map map) {return ok().data("map",map);}

    public static Result object(Object obj) {return ok().data("obj",obj);}

    public static Result page(IPage page){return ok().data("page",page);}

    public static Result err() {
        return new Result(false);
    }

    private boolean success = true;
    private String msg = "操作成功！";
    private Map<String,Object> data = new LinkedHashMap<>();
    private Integer code = 200;


    public Result() {

    }

    public Result(boolean isSuccess) {
        this.success = isSuccess;
        this.code = isSuccess ? 200 : 500;
        this.msg = isSuccess ? "操作成功！" : "操作失败！";
    }

    public Result(String msg) {
        this.msg = msg;
        this.code = 200;
    }

    public Result(Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.data = data;
    }

    public Result(String key, Object obj) {
        this.data(key,obj);
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        this.code = success ? 200 : 500;
    }

    public Result(Integer code, String msg) {
        this.success = code == 200;
        this.msg = msg;
        this.code = code;
    }

    public Result(boolean success, String msg, Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.success = success;
        this.msg = msg;
        this.data = data;
        this.code = success ? 200 : 500;
    }

    public Result(Integer code, String msg, Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.success = code == 200;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public Result success(Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.data = data;
        this.success = true;
        this.code = 200;
        return this;
    }

    public Result success() {
        this.success = true;
        this.code = 200;
        return this;
    }

    public Result error() {
        this.success = false;
        this.msg = "操作失败！";
        this.code = 500;
        return this;
    }

    public Result error(Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.data = data;
        this.msg = "操作失败";
        this.success = false;
        this.code = 500;
        return this;
    }

    public Result error(String msg) {
        this.success = false;
        this.msg = msg;
        this.code = 500;
        return this;
    }

    public Result success(String msg){
        this.success = true;
        this.msg = msg;
        this.code = 200;
        return this;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMsg() {
        return this.msg;
    }

    public Map<String,Object> getData() {
        return this.data;
    }

    public Result data(Map<String,Object> data) {
        if (ObjectUtils.isNull(data)) data = new LinkedHashMap<>();
        this.data = data;
        return this;
    }

    public Result data(String key,Object value) {
        if (ObjectUtils.isNull(this.data)) this.data = new LinkedHashMap<>();
        this.data.put(key,getValue(value));
        return this;
    }

    public Result put(String key, Object value) {
        if (ObjectUtils.isNull(this.data)) this.data = new LinkedHashMap<>();
        this.data.put(key, getValue(value));
        return this;
    }


    public Integer getCode() {
        return this.code;
    }

    private Object getValue(Object value) {
        if (value == null) {
            return  "";
        }
        if (value instanceof Collection) {
            return value;
        }
        return ObjectUtils.isNull(value) ? "" : value;
    }
}
