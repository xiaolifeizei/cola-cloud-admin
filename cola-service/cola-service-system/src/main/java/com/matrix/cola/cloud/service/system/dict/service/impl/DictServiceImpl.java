package com.matrix.cola.cloud.service.system.dict.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.entity.Query;
import com.matrix.cola.cloud.api.entity.system.dict.DictEntity;
import com.matrix.cola.cloud.api.entity.system.dict.DictTree;
import com.matrix.cola.cloud.common.service.AbstractEntityService;
import com.matrix.cola.cloud.service.system.dict.mapper.DictMapper;
import com.matrix.cola.cloud.service.system.dict.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 字典实体类接口实现类
 *
 * @author : cui_feng
 * @since : 2022-05-18 12:53
 */
@Service
public class DictServiceImpl extends AbstractEntityService<DictEntity, DictMapper> implements DictService {

    @Override
    public Result getDictTree(Query<DictEntity> query) {
        return Result.list(DictTree.getDictTree(getList(query)));
    }

    @Override
    protected Result beforeDelete(DictEntity entity) {
        if (ObjectUtil.isNull(entity) || ObjectUtil.isNull(entity.getId())) {
            return Result.err("删除失败，ID不能为空");
        }
        LambdaQueryWrapper<DictEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictEntity::getParentId, entity.getId());
        if (getCount(queryWrapper) > 0) {
            return Result.err("删除失败，该字典下还有子数据，无法删除");
        }
        return Result.ok();
    }

    @Override
    protected Result validate(DictEntity dict) {
        if (ObjectUtil.isNull(dict)) {
            return Result.err("操作失败，字典对象不能为空");
        }
        if (StrUtil.isEmpty(dict.getCode())) {
            return Result.err("操作失败，编码不能为空");
        }
        if (StrUtil.isEmpty(dict.getName())) {
            return Result.err("操作失败，名称不能为空");
        }
        if (StrUtil.isEmpty(dict.getDicValue())) {
            return Result.err("操作失败，键值不能为空");
        }

        LambdaQueryWrapper<DictEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.equals(dict.getParentId(), ColaConstant.TREE_ROOT_ID)) {
            queryWrapper.eq(DictEntity::getCode,dict.getCode()).eq(DictEntity::getParentId,ColaConstant.TREE_ROOT_ID);
            DictEntity dictPO = getOne(queryWrapper);
            if (ObjectUtil.isNotNull(dictPO) && !Objects.equals(dictPO.getId(), dict.getId())) {
                return Result.err("操作失败，该字典编码已经存在");
            }
            queryWrapper.clear();
        } else {
            queryWrapper.eq(DictEntity::getId, dict.getParentId());
            DictEntity dictPO = getOne(queryWrapper);
            if (ObjectUtil.isNull(dictPO)) {
                return Result.err("操作失败，父节点未找到");
            }
            // 如果不只一层
            if (!Objects.equals(dictPO.getParentId(),ColaConstant.TREE_ROOT_ID)) {
                return Result.err("操作失败，字典只能向下添加一层");
            }
            queryWrapper.clear();
        }

        queryWrapper.eq(DictEntity::getCode,dict.getCode()).eq(DictEntity::getDicValue,dict.getDicValue());
        DictEntity dictPO = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(dictPO) && !Objects.equals(dictPO.getId(), dict.getId())) {
            return Result.err("操作失败，该字典信息已经存在");
        }
        return Result.ok();
    }

    @Override
    public Result deleteDict(DictEntity dictPO) {
        if (ObjectUtil.isNull(dictPO)) {
            return Result.err("删除失败，参数不能为空");
        }
        Result result = beforeDelete(dictPO);
        if (!result.isSuccess()) {
            return result;
        }
        if(getMapper().deleteDict(dictPO.getId()) == ColaConstant.YES) {
            return Result.ok();
        }
        return Result.err("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateDict(DictEntity dictPO) {
        Result result = validate(dictPO);
        if (!result.isSuccess()) {
            return result;
        }
        LambdaQueryWrapper<DictEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictEntity::getParentId,dictPO.getId());
        // 如果有子节点，更新时将所有的子节点的字典编码进行同步更新
        if (getCount(queryWrapper) > 0 && getMapper().updateChildrenCode(dictPO) <= 0) {
            return Result.err("修改子节点编码失败");
        }
        if (getMapper().updateById(dictPO) == ColaConstant.YES) {
            return Result.ok();
        }
        return Result.err("修改失败");
    }
}
