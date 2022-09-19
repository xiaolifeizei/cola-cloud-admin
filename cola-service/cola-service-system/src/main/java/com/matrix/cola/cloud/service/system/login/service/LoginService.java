package com.matrix.cola.cloud.service.system.login.service;

import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.BaseService;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;

import java.util.List;

/**
 * 登录服务接口
 *
 * @author cui_feng
 * @since 2022/5/11 20:55
 */
public interface LoginService extends BaseService {


    /**
     * 通过用户名查询用户，用于登陆时鉴权
     * @param loginName 登陆名
     * @return 脱敏后的用户对象
     */
    UserEntity getUserByLoginName(String loginName);

    /**
     * 通过用户id获取用户详细信息
     * @param id 用户id号
     * @return 脱敏后的用户详细信息
     */
    Result getUserInfo(Long id);

    /**
     * 获取分配给该用户的角色编码集合
     * @param userid 用户id
     * @return 授权给该用户的所有权限的编码集合
     */
    List<String> getUserRoleCodeList(Long userid);

    /**
     * 获取当前登录用户的菜单
     * @param userid 用户id
     * @return 获取菜单树
     */
    Result getMenuTreeByUserId(Long userid);

    /**
     * 获取当前登陆用户的按钮
     * @param userid 用户id
     * @return 按钮集合
     */
    Result getButtonsByUserId(Long userid);
}
