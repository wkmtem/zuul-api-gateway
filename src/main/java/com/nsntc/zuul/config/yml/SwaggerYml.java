package com.nsntc.zuul.config.yml;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: SwaggerYml
 * Package: com.nsntc.interview.config.yml
 * Description:
 * @author wkm
 * Create DateTime: 2018/1/4 下午5:37
 * Version: 1.0
 */
@Data
@Component
@RefreshScope
@SpringBootConfiguration
@ConfigurationProperties(prefix = "swagger", ignoreUnknownFields = false)
public class SwaggerYml {

    private String title;
    private String description;
    private String apiVersion;
    private String baseUrl;
    private String createdBy;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;

    private Map<String, String> resource = new HashMap<>(4);
}
