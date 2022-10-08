package com.matrix.cola.cloud.service.system.datalog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.datalog.DataLogEntity;
import org.apache.ibatis.annotations.Delete;

/**
 * 操作日志Mapper接口
 *
 * @author cui_feng
 */
public interface DataLogMapper extends BaseMapper<DataLogEntity> {

    @Delete("delete from system_data_log where id=#{id}")
    int deleteDataLog(Long id);

    @Delete("delete from system_data_log")
    int clearDataLog();
}
