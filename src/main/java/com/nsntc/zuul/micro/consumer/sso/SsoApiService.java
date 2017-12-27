package com.nsntc.zuul.micro.consumer.sso;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.nsntc.commons.bean.Result;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.interview.commons.enums.MicroEnum;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.interview.commons.utils.ResultUtil;
import com.nsntc.zuul.micro.consumer.sso.feign.SsoApiFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class Name: SsoApiService
 * Package: com.nsntc.zuul.micro.consumer.sso
 * Description: 微服务接口调用
 * @author wkm
 * Create DateTime: 2017/12/24 下午11:05
 * Version: 1.0
 */
@Service
public class SsoApiService {

    @Autowired
    private SsoApiFeignClient ssoApiFeignClient;

    /**
     * Method Name: getUserByToken
     * Description: 默认阀值: 5秒内的20次故障, 自动断开, 不再进行通讯
     * Create DateTime: 2017/12/18 下午8:01
     * @param token
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByTokenFallback")
    public Result getUserByToken (String token) {
        String jsonValue = this.ssoApiFeignClient.getUserByToken(token);
        Result result = GsonUtil.toObject(jsonValue, Result.class);
        if (ResultEnum.PARAM_ERROR.getCode().equals(result.getCode())) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), result.getMsg());
        }
        return ResultUtil.success(result.getData());
    }

    /**
     * Method Name: getUserByTokenFallback
     * Description: 容错
     * Create DateTime: 2017/12/18 下午8:02
     * @param token
     * @return
     */
    public Result getUserByTokenFallback(String token) {
        return ResultUtil.error(MicroEnum.MICRO_FAILED);
    }
}
