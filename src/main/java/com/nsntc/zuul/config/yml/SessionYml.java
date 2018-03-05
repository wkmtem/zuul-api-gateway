package com.nsntc.zuul.config.yml;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Class Name: SessionYml
 * Package: com.nsntc.interview.config.yml
 * Description: session配置
 * @author wkm
 * Create DateTime: 2018/2/11 下午7:26
 * Version: 1.0
 */
@Data
@Component
@RefreshScope
@SpringBootConfiguration
@ConfigurationProperties(prefix = "session", ignoreUnknownFields = false)
public class SessionYml {

    /**
     * session失效时间: 秒
     */
    private int maxInactiveIntervalInSeconds;
    /**
     * redis命名空间
     */
    private String redisNamespace;
    /**
     * cookeiName
     */
    private String cookieName;
}
