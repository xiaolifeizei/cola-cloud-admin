package com.matrix.cola.cloud.api.entity.system.dict;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.matrix.cola.cloud.api.common.ColaConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字典树
 *
 * @author : cui_feng
 * @since : 2022-05-18 13:22
 */
@Data
public class DictTree extends DictEntity {

    /**
     * 子对象
     */
    List<DictTree> children = new ArrayList<>();

    /**
     * 生成字典树
     * @param dictList 字典列表
     * @return 字典树集合
     */
    public static List<DictTree> getDictTree(List<DictEntity> dictList) {
        List<DictTree> dictTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(dictList)) {
            return dictTreeList;
        }

        dictTreeList = getTree(ColaConstant.TREE_ROOT_ID,dictList);

        // 处理没有根节点时的数据
        if (ObjectUtils.isEmpty(dictTreeList)) {
            for(DictEntity dictPO : dictList) {
                dictTreeList.add(getDictTree(dictPO));
            }
        }

        return dictTreeList;
    }

    /**
     * 递归生成树
     * @param parentId 父节点id
     * @param dictList 字典集
     * @return ListTree
     */
    private static List<DictTree> getTree(Long parentId,List<DictEntity> dictList) {
        List<DictTree> dictTreeList = new ArrayList<>();
        if (ObjectUtils.isEmpty(dictList)) {
            return dictTreeList;
        }

        for(DictEntity dictPO : dictList) {
            if (Objects.equals(parentId, dictPO.getParentId())) {
                DictTree dictTree = getDictTree(dictPO);
                dictTree.setChildren(getTree(dictPO.getId(),dictList));
                dictTreeList.add(dictTree);
            }
        }
        return dictTreeList;
    }

    /**
     * 填充数据，将DictPO转换成DictTree
     * @param dict 字典
     * @return 字典树
     */
    private static DictTree getDictTree(DictEntity dict) {
        DictTree dictTree = new DictTree();
        BeanUtil.copyProperties(dict,dictTree);
        return dictTree;
    }
}
