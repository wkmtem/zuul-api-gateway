package com.nsntc.zuul.config.cors;


import com.nsntc.commons.constant.CorsConstant;
import com.nsntc.commons.enums.HttpMethodEnum;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

        /** 可自行筛选 */
        corsConfiguration.addAllowedOrigin(CorsConstant.CORS_ALLOWED_ORIGIN);
        corsConfiguration.addAllowedHeader(CorsConstant.CORS_ALLOWED_HEADER);
        corsConfiguration.addAllowedMethod(HttpMethodEnum.GET.getCode());
        corsConfiguration.addAllowedMethod(HttpMethodEnum.POST.getCode());
        corsConfiguration.addAllowedMethod(HttpMethodEnum.PUT.getCode());
        corsConfiguration.addAllowedMethod(HttpMethodEnum.DELETE.getCode());
        corsConfiguration.addAllowedMethod(HttpMethodEnum.HEAD.getCode());
        corsConfiguration.addAllowedMethod(HttpMethodEnum.OPTIONS.getCode());

        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CorsConstant.CORS_MAPPING_PATH, buildConfig());
        return new CorsFilter(source);
    }
}

/** 方法二: 能请求，返回有数据
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
                        .allowedOrigins(CorsConstant.CORS_ALLOWED_ORIGIN)
                        .allowedHeaders(CorsConstant.CORS_ALLOWED_HEADER)
                        .allowedMethods(HttpMethodEnum.GET.getCode(),
                                HttpMethodEnum.POST.getCode(),
                                HttpMethodEnum.PUT.getCode(),
                                HttpMethodEnum.DELETE.getCode(),
                                HttpMethodEnum.HEAD.getCode(),
                                HttpMethodEnum.OPTIONS.getCode())
                        *//** true: ajax请求需添加 xhrFields: {withCredentials: true} *//*
                        .allowCredentials(false)
                        .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(CorsConstant.CORS_MAX_AGE)
                        .maxAge(CorsConstant.CORS_MAX_AGE);
            }
        };
    }
}*/


/** 方法三: 能请求，返回有数据
 *  提示 No 'Access-Control-Allow-Origin’ header is present on the requested resource.
 *      Origin 'http://localhost:63342' is therefore not allowed.
 */
/*@SpringBootConfiguration
public class Blog2MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L)
                .maxAge(3600L);
    }
}*/