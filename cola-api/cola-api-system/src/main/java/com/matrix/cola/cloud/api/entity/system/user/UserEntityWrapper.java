package com.matrix.cola.cloud.api.entity.system.user;

import com.matrix.cola.cloud.api.common.entity.BaseColaEntityWrapper;
import lombok.Data;

/**
 * 用户实体类
 *
 * @author : cui_feng
 * @since : 2022-04-20 13:02
 */
@Data
public class UserEntityWrapper extends BaseColaEntityWrapper {

    /**
     * 姓名
     */
    private String name;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
    **/
    private String ids;

    /**
     * 禁用：0=未禁用，1=已禁用
     */
    private Integer noUse;

    /**
     * 备注
     */
    private String remark;

    /**
     * 新密码
     */
    private String newPassword;
}
