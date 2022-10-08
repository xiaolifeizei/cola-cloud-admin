package com.matrix.cola.cloud.api.entity.system.datalog;

import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;
import lombok.Data;

/**
 * 操作日志实体类
 *
 * @author : cui_feng
 * @since : 2022-04-08 10:05
 */
@Data
public class DataLogEntityWrapper extends BaseColaEntityWrapper {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 操作类型：更新，删除
     */
    private String operation;

    /**
     * 操作之前的数据
     */
    private String beforeData;

    /**
     * 操作之后的数据
     */
    private String afterData;
}
