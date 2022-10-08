package com.matrix.cola.cloud.service.system.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.cola.cloud.api.entity.system.dict.DictEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 字典Mapper
 *
 * @author : cui_feng
 * @since : 2022-05-18 12:55
 */
public interface DictMapper extends BaseMapper<DictEntity> {

    /**
     * 物理删除字典
     * @param id 字典id
     * @return 删除的条数
     */
    @Delete("delete from system_dict where id=#{id}")
    int deleteDict(Long id);

    /**
     * 当修改父级编码时同时更新子记录编码
     * @param dict 字典对象
     * @return 更新的条数
     */
    @Update("update system_dict set code=#{dict.code} where parent_id = #{dict.id}")
    int updateChildrenCode(@Param("dict") DictEntity dict);
}
