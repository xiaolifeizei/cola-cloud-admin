package com.matrix.cola.cloud.auth.support;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 无加密
 *
 * @author : cui_feng
 * @since : 2022-04-20 15:19
 */
public class ColaNoOpPasswordEncoder implements PasswordEncoder {
    /**
     * 不加密
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }

    /**
     * 单例
     * @return 实例化当前对象
     */
    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private static final PasswordEncoder INSTANCE = new ColaNoOpPasswordEncoder();

    private ColaNoOpPasswordEncoder() {
    }
}
