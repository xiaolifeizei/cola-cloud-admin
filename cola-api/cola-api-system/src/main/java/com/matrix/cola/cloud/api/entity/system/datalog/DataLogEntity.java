package com.matrix.cola.cloud.api.entity.system.datalog;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 操作日志实体类
 *
 * @author : cui_feng
 * @since : 2022-04-08 10:05
 */
@Data
@TableName("system_data_log")
public class DataLogEntity extends BaseColaEntity {

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

    /**
     * 创建更新日志
     * @param tableName 表名，使用中文描述如：客户管理
     * @param before 更新前的对象
     * @param after 更新后的对象
     * @return DataLogEntity
     */
    public static DataLogEntity createUpdateLog(@NotNull String tableName,@NotNull Object before,@NotNull Object after) {
        DataLogEntity dataLogEntity = new DataLogEntity();
        dataLogEntity.setTableName(tableName);
        dataLogEntity.setBeforeData(JSON.toJSONString(before));
        dataLogEntity.setAfterData(JSON.toJSONString(after));
        return dataLogEntity;
    }

    /**
     * 创建删除日志
     * @param tableName 表名，使用中文描述如：客户管理
     * @param before 删除前的对象
     * @return DataLogEntity
     */
    public static DataLogEntity createDeleteLog(@NotNull String tableName, @NotNull Object before) {
        DataLogEntity dataLogEntity = new DataLogEntity();
        dataLogEntity.setTableName(tableName);
        dataLogEntity.setBeforeData(JSON.toJSONString(before));
        return dataLogEntity;
    }
}
