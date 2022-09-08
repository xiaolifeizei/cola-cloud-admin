package com.matrix.cola.cloud.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

/**
 * Druid配置
 *
 * @author : cui_feng
 * @since : 2022-06-29 09:21
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DruidConfig {

    /**
     * 配置 Druid 监控界面
     */
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet(){
        ServletRegistrationBean<Servlet> srb = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        //设置控制台管理用户
        srb.addInitParameter("loginUsername","root");
        srb.addInitParameter("loginPassword","root");
        //是否可以重置数据
        srb.addInitParameter("resetEnable","false");
        return srb;
    }

    /**
     * 去除监控页面底部的广告
     */
    @Bean
    public FilterRegistrationBean<Filter> removeDruidFilterRegistrationBean() {
        // 提取common.js的配置路径
        String commonJsPattern = "/druid/js/common.js";
        final String filePath = "support/http/resources/js/common.js";

        // 创建filter进行过滤
        Filter filter = new Filter() {
            public void init(FilterConfig filterConfig) throws ServletException {
            }
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
                // 重置缓冲区，响应头不会被重置
                response.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }

            @Override
            public void destroy(){
            }
        };

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}
