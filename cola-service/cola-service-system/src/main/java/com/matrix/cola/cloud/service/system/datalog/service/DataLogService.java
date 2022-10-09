package com.matrix.cola.cloud.service.system.datalog.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.BaseColaEntity;
import com.matrix.cola.cloud.api.common.service.BaseColaEntityService;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;

/**
 * 操作日志接口
 *
 * @author cui_feng
 * @since : 2022-04-20 14:18
 */
public interface DataLogService extends BaseColaEntityService<DataLogEntity> {

    /**
     * 记录修改日志
     * @param tableName 表名
     * @param before 更新前的数据
     * @param after 更新后的数据
     */
    void addUpdateLog(String tableName, BaseColaEntity before, BaseColaEntity after);

    /**
     * 记录修改日志
     * @param dataLogEntity 数据日志
     */
    void addUpdateLog(DataLogEntity dataLogEntity);

    /**
     * 记录删除日志
     * @param tableName 表名
     * @param before 删除前的记录
     */
    void addDeleteLog(String tableName, BaseColaEntity before);

    /**
     * 记录删除日志
     * @param dataLogEntity 数据日志
     */
    void addDeleteLog(DataLogEntity dataLogEntity);

    /**
     * 物理删除数据日志
     * @param dataLogEntity 数据日志实体类
     * @return 统一结果
     */
    Result deleteDataLog(DataLogEntity dataLogEntity);

    /**
     * 删除全部数据日志
     * @return 统一结果
     */
    Result clearDataLog();
}
