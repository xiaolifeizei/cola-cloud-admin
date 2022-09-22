package com.matrix.cola.cloud.auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.api.feign.system.login.LoginServiceFeign;
import com.matrix.cola.cloud.common.utils.SecurityConst;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证用户详情服务类
 *
 * @author : cui_feng
 * @since : 2022-04-20 14:08
 */
@Service
@AllArgsConstructor
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    LoginServiceFeign loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {

        UserEntity userPO = loginService.getUserByLoginName(username);

        if (userPO == null) {
            throw new UsernameNotFoundException("用户名或密码验证失败");
        }

        // 获取当前用户的角色编码
        List<String> roleCodeList = loginService.getUserRoleCodeList(userPO.getId());
        if (ObjectUtil.isEmpty(roleCodeList)) {
            roleCodeList = new ArrayList<>();
            roleCodeList.add(ColaConstant.DEFAULT_ROLE_CODE);
        }

        // 密码类型，用于匹配密码编码器
        userPO.setPassword(SecurityConst.COLA_ENCODE_KEY +userPO.getPassword());

        // 填充数据
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUser(userPO);
        securityUser.setPermissionList(roleCodeList);

        return securityUser;
    }
}
