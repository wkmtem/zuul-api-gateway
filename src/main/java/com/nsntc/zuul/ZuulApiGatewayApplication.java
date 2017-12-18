package com.nsntc.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Class Name: ZuulApiGatewayApplication
 * Package: com.nsntc.zuul
 * Description: 
 * @author wkm
 * Create DateTime: 2017/12/16 上午5:36
 * Version: 1.0
 */
/** 启用Feign客户端 */
@EnableFeignClients
/** 开启容错机制 */
@EnableHystrix
/** 启用Zuul代理 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayApplication.class, args);
	}
}
