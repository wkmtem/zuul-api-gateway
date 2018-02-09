package com.nsntc.zuul.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Class Name: WebAdapter
 * Package: com.nsntc.zuul.interceptor
 * Description: 拦截器链
 * @author wkm
 * Create DateTime: 2018/2/8 下午9:36
 * Version: 1.0
 */
@SpringBootConfiguration
public class WebAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 主要方法说明：
         * addPathPatterns 添加拦截
         * excludePathPatterns 排除拦截
         */
        registry.addInterceptor(new CapabilityWatchHandlerInterceptor()).addPathPatterns("/*");
        super.addInterceptors(registry);
    }
}
