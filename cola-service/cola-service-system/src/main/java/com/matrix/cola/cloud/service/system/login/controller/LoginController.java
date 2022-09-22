package com.matrix.cola.cloud.service.system.login.controller;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.utils.WebUtil;
import com.matrix.cola.cloud.service.system.login.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录控制器
 *
 * @author : cui_feng
 * @since : 2022-09-16 11:08
 */
@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    final LoginService loginService;

    /**
     * 登陆时获取用户信息
     *
     * @return 用户详细信息
     */
    @PostMapping("/getUserInfo")
    public Result info() {
        UserEntity userPO = WebUtil.getUser();
        if (ObjectUtil.isNull(userPO)) {
            return Result.err("没有获取到当前用户，请重新登陆后再试");
        }
        return loginService.getUserInfo(userPO.getId());
    }

    /**
     * 获取登录用户的菜单
     * @return 菜单分页对象
     */
    @PostMapping("/getMenuTreeByUser")
    public Result getMenuTreeByUser() {
        UserEntity userPO= WebUtil.getUser();
        if (ObjectUtil.isNull(userPO)) {
            return Result.err("用户不能为空");
        }
        return loginService.getMenuTreeByUserId(userPO.getId());
    }

    /**
     * 获取登录用户的按钮
     * @return 菜单分页对象
     */
    @PostMapping("/getButtonsByUser")
    public Result getButtonsByUser() {
        UserEntity userPO= WebUtil.getUser();
        if (ObjectUtil.isNull(userPO)) {
            return Result.err("用户不能为空");
        }
        return loginService.getButtonsByUserId(userPO.getId());
    }

//    @PostMapping("/refreshToken")
//    public Result refreshToken() {
//        try {
//            return Result.ok().put("token", JwtTokenUtil.createToken(WebUtil.getUser()));
//        } catch (Exception ignore) {
//        }
//        // 刷新token失败反回空
//        return Result.ok();
//    }

    /**
     * 通过用户名获取登录用户
     * @param loginName 用户名
     * @return UserEntity
     */
    @PostMapping("/getUserByLoginName")
    public UserEntity getUserByLoginName(String loginName) {
        return loginService.getUserByLoginName(loginName);
    }

    /**
     * 获取分配给该用户的角色编码集合
     * @param userid 用户id
     * @return 授权给该用户的所有权限的编码集合
     */
    @PostMapping("/getUserRoleCodeList")
    public List<String> getUserRoleCodeList(Long userid) {
        return loginService.getUserRoleCodeList(userid);
    }
}
