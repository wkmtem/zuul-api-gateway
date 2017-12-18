package com.nsntc.zuul.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Class Name: SsoFeignClient
 * Package: com.spring.cloud.feign
 * Description: 申明Feign客户端, 并指明服务id
 * @author wkm
 * Create DateTime: 2017/12/16 上午2:16
 * Version: 1.0
 */
@FeignClient(value = "sb-interview-sso")
public interface SsoFeignClient {

    /**
     * Method Name: getUserByToken
     * Description: 不能用@GetMapping组合替换
     * Create DateTime: 2017/12/18 下午7:52
     * @param token
     * @return
     */
    @RequestMapping(value = "sso/api/user/{token}", method = RequestMethod.GET)
    String getUserByToken(@PathVariable("token") String token);

    /**
     * Method Name: saveOperationLog
     * Description:
     * Create DateTime: 2017/12/19 上午1:19
     * @param param
     */
    @RequestMapping(value = "sso/api/operation", method = RequestMethod.POST)
    void saveOperationLog(@RequestParam Map<String, Object> param);
}
