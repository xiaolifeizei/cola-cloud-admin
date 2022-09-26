package com.matrix.cola.cloud.common.utils;

/**
 * 鉴权常量定义
 *
 * @author cui_feng
 * @since 2022/4/23 23:05
 */
public class SecurityConst {

    public static final String COLA_ENCODE = "cola";
    public static final String COLA_ENCODE_KEY = "{cola}";

    public static final String BCRYPT_ENCODE = "bcrypt";
    public static final String BCRYPT_ENCODE_KEY = "{bcrypt}";

    public static final String NOOP_ENCODE = "noop";
    public static final String NOOP_ENCODE_KEY = "{noop}";

    public static final String PBKDF2_ENCODE = "pbkdf2";
    public static final String PBKDF2_ENCODE_KEY = "{pbkdf2}";

    public static final String SCRYPT_ENCODE = "scrypt";
    public static final String SCRYPT_ENCODE_KEY = "scrypt";

    /**
     * 验签密钥
     */
    public static final String JWT_KEY = "MatrixColaCloud";

    /**
     * 用户授权jwt令牌
     */
    public static final String APPROVE_JWT_KEY = "MatrixColaCloudOAuth2Approve";

    public static final String TOKEN_KEY = "token";

    public static final String TOKEN_PREFIX = "bearer ";


}
