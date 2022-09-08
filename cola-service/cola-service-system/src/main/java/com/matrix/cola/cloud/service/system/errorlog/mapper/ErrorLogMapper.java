package com.matrix.cola.cloud.service.system.errorlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.errorlog.ErrorLogEntity;
import org.apache.ibatis.annotations.Delete;

/**
 * 系统错误mapper
 *
 * @author : cui_feng
 * @since : 2022-06-10 12:43
 */
public interface ErrorLogMapper extends BaseMapper<ErrorLogEntity> {

    @Delete("delete from system_error_log")
    int clearErrorLog();
}
