package com.nsntc.zuul.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Class Name: SbInterviewSsoFeignClient
 * Package: com.spring.cloud.feign
 * Description: 申明Feign客户端, 并指明服务id
 * @author wkm
 * Create DateTime: 2017/12/16 上午2:16
 * Version: 1.0
 */
@FeignClient(value = "sb-interview-sso")
public interface SbInterviewSsoFeignClient {

    /**
     * Method Name: getUserByToken
     * Description: 不能用@GetMapping组合替换
     * Create DateTime: 2017/12/18 下午7:52
     * @param token
     * @return
     */
    @RequestMapping(value = "api/user/{token}", method = RequestMethod.GET)
    String getUserByToken(@PathVariable("token") String token);
}
