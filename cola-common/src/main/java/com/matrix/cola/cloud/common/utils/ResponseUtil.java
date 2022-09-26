package com.matrix.cola.cloud.common.utils;

import cn.hutool.json.JSONUtil;
import com.matrix.cola.cloud.api.common.Result;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.http.Consts.UTF_8;

/**
 * response工具类
 *
 * @author : cui_feng
 * @since : 2022-09-05 17:07
 */
public class ResponseUtil {

    @SneakyThrows
    public static void out (HttpServletResponse response, Result result) {
        ServletOutputStream output = response.getOutputStream();
        response.setStatus(result.getCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE );
        try {
            output.write(JSONUtil.toJsonStr(result).getBytes(UTF_8));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
