package com.matrix.cola.cloud.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * response工具类
 *
 * @author : cui_feng
 * @since : 2022-09-05 17:07
 */
public class ResponseUtil {

    public static Map<String, Object> out(int status, String msg) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("success", status == 200);
        map.put("code", status);
        map.put("msg", msg);
        map.put("data", new LinkedHashMap<>());
        return map;
    }
}
