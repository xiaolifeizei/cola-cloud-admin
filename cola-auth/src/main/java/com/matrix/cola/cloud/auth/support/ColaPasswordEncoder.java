package com.matrix.cola.cloud.auth.support;

import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 默认密码加密采用sha1(md5())方式加密
 *
 * @author : cui_feng
 * @since : 2022-04-20 15:06
 */
public class ColaPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return SecureUtil.sha1(SecureUtil.md5((String)rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }

    public static void main(String[] args) {
        ColaPasswordEncoder en = new ColaPasswordEncoder();
        System.out.println(en.encode("123"));
    }
}
