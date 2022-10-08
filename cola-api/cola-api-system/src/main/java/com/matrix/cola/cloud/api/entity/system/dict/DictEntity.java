package com.matrix.cola.cloud.api.entity.system.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.cola.cloud.api.common.entity.BaseEntity;
import lombok.Data;

/**
 * 字典实体类
 *
 * @author : cui_feng
 * @since : 2022-05-18 12:49
 */
@Data
@TableName("system_dict")
public class DictEntity extends BaseEntity {

    /**
     * id号
     * 默认数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 字典名
     */
    private String name;

    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典值
     */
    private String dicValue;


}
