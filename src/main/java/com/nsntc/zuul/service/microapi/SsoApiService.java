package com.nsntc.zuul.service.microapi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.feign.SsoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Class Name: SsoApiService
 * Package: com.nsntc.zuul.service.microapi
 * Description: 微服务接口调用
 * @author wkm
 * Create DateTime: 2017/12/18 下午8:04
 * Version: 1.0
 */
@Service
public class SsoApiService {

    @Autowired
    private SsoFeignClient ssoFeignClient;

    /**
     * Method Name: getUserByToken
     * Description: 默认阀值: 5秒内的20次故障, 自动断开, 不再进行通讯
     * Create DateTime: 2017/12/18 下午8:01
     * @param token
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByTokenFallback")
    public String getUserByToken (String token) {
        return this.ssoFeignClient.getUserByToken(token);
    }

    /**
     * Method Name: getUserByTokenFallback
     * Description: 容错
     * Create DateTime: 2017/12/18 下午8:02
     * @param token
     * @return
     */
    public String getUserByTokenFallback(String token) {
        return ResultEnum.SYSTEM_ERROR.getMessage();
    }


    /**
     * Method Name: saveOperationLog
     * Description:
     * Create DateTime: 2017/12/19 上午1:22
     * @param param
     */
    @HystrixCommand(fallbackMethod = "saveOperationLogFallback")
    public void saveOperationLog(Map<String, Object> param) {
        this.ssoFeignClient.saveOperationLog(param);
    }

    /**
     * Method Name: saveOperationLogFallback
     * Description: 容错
     * Create DateTime: 2017/12/19 上午1:23
     */
    public void saveOperationLogFallback(Map<String, Object> param) {

    }
}
