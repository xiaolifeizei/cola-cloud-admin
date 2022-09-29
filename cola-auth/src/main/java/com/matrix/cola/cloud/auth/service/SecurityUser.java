package com.matrix.cola.cloud.auth.service;

import cn.hutool.core.util.StrUtil;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 认证用户类
 *
 * @author : cui_feng
 * @since : 2022-04-20 14:05
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * 当前登陆用户
     */
    private UserEntity currentUser;

    /**
     * 当前用户权限
     */
    private List<String> permissionList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String perminssion : this.permissionList) {
            if (StrUtil.isEmpty(perminssion)) continue;
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(perminssion);
            authorities.add(simpleGrantedAuthority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUser.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUser.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return currentUser.getNoUse() != null && currentUser.getNoUse() != 1;
    }
}
