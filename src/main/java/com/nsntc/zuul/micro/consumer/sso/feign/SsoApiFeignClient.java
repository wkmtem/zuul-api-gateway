package com.nsntc.zuul.micro.consumer.sso.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Class Name: SsoApiFeignClient
 * Package: com.nsntc.zuul.micro.consumer.sso.feign
 * Description: 申明Feign客户端, 并指明服务id
 * @author wkm
 * Create DateTime: 2017/12/24 下午11:16
 * Version: 1.0
 */
@FeignClient(value = "interview-sso")
public interface SsoApiFeignClient {

    /**
     * Method Name: getUserByToken
     * Description: 根据token获取用户
     * Create DateTime: 2017/12/18 下午7:52
     * @param token
     * @return
     */
    @RequestMapping(value = "sso/api/user/{token}", method = RequestMethod.GET)
    String getUserByToken(@PathVariable("token") String token);

    /**
     * Method Name: saveOperationLog
     * Description: 保存操作日志
     * Create DateTime: 2017/12/19 上午1:19
     * @param param
     */
    @RequestMapping(value = "sso/api/operation", method = RequestMethod.POST)
    void saveOperationLog(@RequestParam Map<String, Object> param);
}
