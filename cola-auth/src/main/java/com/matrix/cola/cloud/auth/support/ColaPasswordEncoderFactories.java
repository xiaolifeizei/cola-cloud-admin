package com.matrix.cola.cloud.auth.support;

import com.matrix.cola.cloud.common.utils.SecurityConst;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码编码器工厂，通过标识自动选择密码加载器
 *
 * @author : cui_feng
 * @since : 2022-04-20 15:05
 */
public class ColaPasswordEncoderFactories {

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>(16);
        encoders.put(SecurityConst.COLA_ENCODE, new ColaPasswordEncoder());
        encoders.put(SecurityConst.BCRYPT_ENCODE, new BCryptPasswordEncoder());
        encoders.put(SecurityConst.NOOP_ENCODE, ColaNoOpPasswordEncoder.getInstance());
        encoders.put(SecurityConst.PBKDF2_ENCODE, new Pbkdf2PasswordEncoder());
        encoders.put(SecurityConst.SCRYPT_ENCODE, new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(SecurityConst.COLA_ENCODE, encoders);
    }

    private ColaPasswordEncoderFactories() {
    }
}
