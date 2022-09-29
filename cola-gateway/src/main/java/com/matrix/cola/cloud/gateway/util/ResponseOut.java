package com.matrix.cola.cloud.gateway.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : cui_feng
 * @since : 2022-09-29 16:31
 */
public class ResponseOut {

    public static Map<String, Object> out(int status, String msg) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("success", status == 200);
        map.put("code", status);
        map.put("msg", msg);
        map.put("data", new LinkedHashMap<>());
        return map;
    }

    public static Map<String, Object> out(String msg) {
        return out(200, msg);
    }
}
