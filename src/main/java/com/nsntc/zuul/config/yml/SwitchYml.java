package com.nsntc.zuul.config.yml;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Class Name: SwitchYml
 * Package: com.nsntc.zuul.config.yml
 * Description: 全局开关
 * @author wkm
 * Create DateTime: 2017/12/30 下午5:35
 * Version: 1.0
 */
@Data
@Component
@RefreshScope
@SpringBootConfiguration
@ConfigurationProperties(prefix = "global", ignoreUnknownFields = false)
public class SwitchYml {

    /**
     * true: 开, false: 关
     */
    private Boolean switchVal = true;
}
