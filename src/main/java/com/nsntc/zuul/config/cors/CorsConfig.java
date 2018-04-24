package com.nsntc.zuul.config.cors;

import com.nsntc.zuul.constant.CorsConstant;
import com.nsntc.commons.enums.HttpMethodEnum;
import com.nsntc.commons.utils.RequestUtil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * Class Name: CorsConfig
 * Package: com.nsntc.zuul.config.cors
 * Description: 跨域
 * @author wkm
 * Create DateTime: 2018/1/12 下午3:49
 * Version: 1.0
 */
@SpringBootConfiguration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /** 1、设置访问源地址 */
        corsConfiguration.addAllowedOrigin(CorsConstant.CORS_ALLOWED_STAR);
        /** 2、设置访问源请求头 */
        corsConfiguration.addAllowedHeader(CorsConstant.CORS_ALLOWED_STAR);
        /** 3、设置访问源请求方法 */
        corsConfiguration.setAllowedMethods(Arrays.asList(CorsConstant.CORS_ALLOWED_METHODS));
        /** 4、设置允许携带证书(cookie), 要求cookie的域必须是两个子域的顶级域
         *  ajax 添加语句
         *  crossDomain: true,
         *  xhrFields: { withCredentials: true }
         */
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(CorsConstant.CORS_MAX_AGE);

        return corsConfiguration;
    }

    /**
     * Method Name: corsFilter
     * Description: 返回带排序值的跨域过滤器
     * Create DateTime: 2018/3/13 下午1:58
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        /** 5、对接口配置跨域设置 */
        source.registerCorsConfiguration(CorsConstant.CORS_MAPPING_PATH, buildConfig());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
        /** filter排序值 */
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    /**
     * Method Name: corsFilter
     * Description: 返回跨域过滤器
     * Create DateTime: 2018/3/13 下午1:57
     */
    /*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        *//** 5、对接口配置跨域设置 *//*
        source.registerCorsConfiguration(CorsConstant.CORS_MAPPING_PATH, buildConfig());
        return new CorsFilter(source);
    }*/
}


/** 方法二: servlet过滤器方式, 实测有效 */
/*@SpringBootConfiguration
public class CorsConfig {

    @Bean(destroyMethod = "destroy")
    public CorsFilter corsFilter() {
        return new com.nsntc.zuul.filter.CorsFilter();
    }
}*/

/** 方法三: 能请求，返回有数据(缺少对response支持)
 *  提示 No 'Access-Control-Allow-Origin’ header is present on the requested resource.
 *      Origin 'http://localhost:63342' is therefore not allowed.
 */
/*@SpringBootConfiguration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(CorsConstant.CORS_MAPPING_PATH)
                        .allowedHeaders(CorsConstant.CORS_ALLOWED_HEADER)
                        .allowedMethods(CorsConstant.CORS_ALLOWED_METHODS)
                        *//**
                         * 允许携带证书(cookie), 要求cookie的域必须是两个子域的顶级域
                         * ajax 添加语句
                         * crossDomain: true,
                         * xhrFields: { withCredentials: true }
                         *//*
                        .allowCredentials(false)
                        .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(CorsConstant.CORS_MAX_AGE)
                        .maxAge(CorsConstant.CORS_MAX_AGE)
                        .allowedOrigins(CorsConstant.CORS_ALLOWED_ORIGIN);
            }
        };
    }
}*/


/** 方法四: 能请求，返回有数据(缺少对response支持)
 *  提示 No 'Access-Control-Allow-Origin’ header is present on the requested resource.
 *      Origin 'http://localhost:63342' is therefore not allowed.
 */

/*@SpringBootConfiguration
public class CorsConfig extends WebMvcConfigurationSupport {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(CorsConstant.CORS_MAPPING_PATH)
                .allowedOrigins(CorsConstant.CORS_ALLOWED_ORIGIN)
                .allowedHeaders(CorsConstant.CORS_ALLOWED_HEADER)
                .allowedMethods(CorsConstant.CORS_ALLOWED_METHODS)
                *//**
                 * 允许携带证书(cookie), 要求cookie的域必须是两个子域的顶级域
                 * ajax 添加语句
                 * crossDomain: true,
                 * xhrFields: { withCredentials: true }
                 *//*
                .allowCredentials(false)
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(CorsConstant.CORS_MAX_AGE)
                .maxAge(CorsConstant.CORS_MAX_AGE);
    }
}*/


