package com.matrix.cola.cloud.service.system.dict.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.common.service.BaseEntityService;
import com.matrix.cola.cloud.api.entity.system.dict.DictEntity;

/**
 * 字典实体类接口
 *
 * @author : cui_feng
 * @since : 2022-05-18 12:52
 */
public interface DictService extends BaseEntityService<DictEntity> {

    /**
     * 通过前端查询条件获取字典树
     * @param query 查询条件
     * @return 字典树
     */
    Result getDictTree(Query<DictEntity> query);

    /**
     * 删除一条字典，物理删除
     * @param dictPo 字典实体类，必须包含id
     * @return 返回值
     */
    Result deleteDict(DictEntity dictPo);

    /**
     * 修改字典
     * @param dictPO 字典实体类，必须包含id
     * @return 返回值
     */
    Result updateDict(DictEntity dictPO);
}
