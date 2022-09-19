package com.matrix.cola.cloud.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.cola.cloud.api.common.Result;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    public static void out (HttpServletResponse response, Result result) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(result.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE );
        try {
            objectMapper.writeValue(response.getWriter(),result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
