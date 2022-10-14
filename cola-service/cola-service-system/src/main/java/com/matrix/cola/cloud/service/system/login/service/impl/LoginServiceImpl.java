package com.matrix.cola.cloud.service.system.login.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.menu.MenuEntity;
import com.matrix.cola.cloud.api.entity.system.menu.VueMenu;
import com.matrix.cola.cloud.api.entity.system.role.RoleEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.service.system.login.service.LoginService;
import com.matrix.cola.cloud.service.system.menu.service.MenuService;
import com.matrix.cola.cloud.service.system.role.service.RoleService;
import com.matrix.cola.cloud.service.system.role.service.RoleUserService;
import com.matrix.cola.cloud.service.system.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录服务接口实现类
 *
 * @author cui_feng
 * @since 2022/5/11 20:57
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private UserService userService;

    private RoleUserService roleUserService;

    private RoleService roleService;

    private CacheProxy cacheProxy;

    private MenuService menuService;

    @Override
    public UserEntity getUserByLoginName(String loginName) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getLoginName, loginName).eq(UserEntity::getNoUse,ColaConstant.NO);
        return userService.getOne(queryWrapper);
    }

    @Override
    public Result getUserInfo(Long id) {
        UserEntity userPO = userService.getOne(id);
        if (ObjectUtil.isNull(userPO)) {
            return Result.err("没有找到该用户");
        }

        // 隐藏敏感信息
        userPO.setPassword(null);
        userPO.setIds(DesensitizedUtil.idCardNum(userPO.getIds(),6,4));
        userPO.setPhone(DesensitizedUtil.mobilePhone(userPO.getPhone()));

        // 登陆时先删除原有缓存
        cacheProxy.evict(ColaCacheName.USER_ROLE_CODES,id.toString());
        cacheProxy.evict(ColaCacheName.USER_ROLE_IDS,id.toString());
        cacheProxy.evict(ColaCacheName.USER_MENUS,id.toString());

        List<String> roleCodesList = getUserRoleCodeList(id);
        if (ObjectUtil.isEmpty(roleCodesList)) {
            roleCodesList = new ArrayList<>();
            // 默认添加user角色
            roleCodesList.add(ColaConstant.DEFAULT_ROLE_CODE);
        }
        return Result.ok().put("user",userPO).put("roles",roleCodesList).put("name",userPO.getName());
    }

    @Override
    public List<String> getUserRoleCodeList(Long userid) {
        if (userid == null) {
            return Collections.emptyList();
        }
        // 从缓存中获取
        return cacheProxy.getObjectFromLoader(ColaCacheName.USER_ROLE_CODES,userid.toString(),()->{
            return getRoleCodeList(userid);
        });
    }

    /**
     * 通过用户id获取该用户的角色id列表
     * @param userid 用户id
     * @return 角色id集合
     */
    private List<Long> getRoleIdListByUserId(Long userid) {
        return cacheProxy.getObjectFromLoader(ColaCacheName.USER_ROLE_IDS,userid.toString(),()->{
            return roleUserService.getRoleIdsByUserId(userid);
        });
    }

    /**
     * 通过用户ID查询分配的角色编码集合
     * @param userid 用户id
     * @return 角色编码集合
     */
    private List<String> getRoleCodeList(Long userid) {

        List<Long> roleIdList = getRoleIdListByUserId(userid);

        if (ObjectUtil.isEmpty(roleIdList)) {
            return null;
        }

        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleEntity::getId,roleIdList);
        List<RoleEntity> roleList = roleService.getList(queryWrapper);

        String roleCodes;
        if (ObjectUtil.isEmpty(roleList)) {
            return null;
        }

        roleCodes = roleList.stream().map(RoleEntity::getCode).collect(Collectors.joining(","));
        return StrUtil.split(roleCodes,",");
    }

    @Override
    public Result getMenuTreeByUserId(Long userid) {

        List<VueMenu> vueMenuList = new ArrayList<>();
        List<Long> roleIdList = getRoleIdListByUserId(userid);
        if (ObjectUtil.isNotEmpty(roleIdList)) {
            List<MenuEntity> menuList = menuService.getMenuByRoleIds(roleIdList);
            if (ObjectUtil.isNotEmpty(menuList)) {

                //取消缓存菜单 @date 2022-05-15
                // List<MenuPO> menuList = cacheProxy.getObjectFromLoader(ColaCacheName.USER_MENUS,userid.toString(),()->{
                // return menuService.getMenuByRoleIds(roleIdList);
                // });

                // 生成菜单
                vueMenuList = VueMenu.getVueTree(ColaConstant.TREE_ROOT_ID,menuList);
            }
        }

        return Result.ok().put("menu", vueMenuList);
    }

    @Override
    public Result getButtonsByUserId(Long userid) {
        if (ObjectUtil.isNull(userid)) {
            return null;
        }

        List<Long> roleIdList = getRoleIdListByUserId(userid);
        List<MenuEntity> buttonList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(roleIdList)) {
            buttonList = menuService.getButtonsByRoleIds(roleIdList);
        }

        return Result.ok().put("buttons",buttonList);
    }
}
