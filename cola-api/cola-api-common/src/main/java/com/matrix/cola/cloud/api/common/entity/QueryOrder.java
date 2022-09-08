package com.matrix.cola.cloud.api.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装排序条件
 *
 * @author : cui_feng
 * @since : 2022-05-18 10:33
 */
@Data
public class QueryOrder implements Serializable {

    /**
     * 字段名
     */
    private String name;

    /**
     * 是否升序
     */
    private boolean asc = true;

}
