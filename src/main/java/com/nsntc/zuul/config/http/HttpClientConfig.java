package com.nsntc.zuul.config.http;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Class Name: HttpClientConfig
 * Package: com.spring.cloud.config
 * Description:
 * @author wkm
 * Create DateTime: 2017/12/14 上午4:20
 * Version: 1.0
 */
@SpringBootConfiguration
public class HttpClientConfig {

    /**
     * Method Name: restTemplate
     * Description: RestTemplate底层okHttp3实现, 开启负载均衡
     * Create DateTime: 2017/12/18 下午7:15
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
