package com.nsntc.zuul.config.cors;

import com.nsntc.commons.constant.CorsConstant;
import com.nsntc.commons.enums.HttpMethodEnum;
import com.nsntc.commons.utils.RequestUtil;
import org.springframework.boot.SpringBootConfiguration;
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

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CorsConstant.CORS_MAPPING_PATH, buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /** 可自行筛选 */
        corsConfiguration.addAllowedOrigin(CorsConstant.CORS_ALLOWED_ORIGIN);
        corsConfiguration.addAllowedHeader(CorsConstant.CORS_ALLOWED_HEADER);
        corsConfiguration.setAllowedMethods(Arrays.asList(CorsConstant.CORS_ALLOWED_METHODS));
        /**
         * 允许携带证书(cookie), 要求cookie的域必须是两个子域的顶级域
         * ajax 添加语句
         * crossDomain: true,
         * xhrFields: { withCredentials: true }
         */
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(CorsConstant.CORS_MAX_AGE);

        return corsConfiguration;
    }
}

/** 方法二: 过滤器, 实测有效 */
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


