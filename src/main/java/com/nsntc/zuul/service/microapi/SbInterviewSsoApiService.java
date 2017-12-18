package com.nsntc.zuul.service.microapi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.nsntc.commons.bean.Result;
import com.nsntc.commons.enums.ResultEnum;
import com.nsntc.zuul.feign.SbInterviewSsoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class Name: SbInterviewSsoApiService
 * Package: com.nsntc.zuul.service.microapi
 * Description: 微服务接口调用
 * @author wkm
 * Create DateTime: 2017/12/18 下午8:04
 * Version: 1.0
 */
@Service
public class SbInterviewSsoApiService {

    @Autowired
    private SbInterviewSsoFeignClient sbInterviewSsoFeignClient;

    /**
     * Method Name: getUserByToken
     * Description: 默认阀值: 5秒内的20次故障, 自动断开, 不再进行通讯
     * Create DateTime: 2017/12/18 下午8:01
     * @param token
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByTokenFallbackMethod")
    public String getUserByToken (String token) {
        return this.sbInterviewSsoFeignClient.getUserByToken(token);
    }

    /**
     * Method Name: xxxFallbackMethod
     * Description: 容错机制
     * Create DateTime: 2017/12/18 下午8:02
     * @param token
     * @return
     */
    public String getUserByTokenFallbackMethod(String token) {
        return ResultEnum.SYSTEM_ERROR.getMessage();
    }
}
