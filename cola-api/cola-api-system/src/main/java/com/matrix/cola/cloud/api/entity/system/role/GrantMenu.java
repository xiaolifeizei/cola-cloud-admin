package com.matrix.cola.cloud.api.entity.system.role;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 给角色分配菜单的包装类
 *
 * @author : cui_feng
 * @since : 2022-06-02 09:58
 */
@Data
public class GrantMenu implements Serializable {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 要分配的角色菜单集合
     */
    private List<RoleMenuEntity> list = new ArrayList<>();
}
