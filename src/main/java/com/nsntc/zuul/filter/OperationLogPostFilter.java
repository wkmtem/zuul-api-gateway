package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.zuul.bean.RedisUser;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.micro.consumer.sso.SsoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: OperationLogPostFilter
 * Package: com.nsntc.zuul.filter
 * Description: 操作日志后置过滤器
 * @author wkm
 * Create DateTime: 2017/12/18 下午10:15
 * Version: 1.0
 */
@Component
@Slf4j
public class OperationLogPostFilter extends ZuulFilter {

    @Autowired
    private SsoApiService ssoApiService;

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return  (boolean) requestContext.get(ZuulConstant.NEXT_FILTER);
    }

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.POST.getCode();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() {
        this.operationLog();
        return null;
    }

    /**
     * Method Name: operationLog
     * Description: 操作日志
     * Create DateTime: 2017/12/19 上午1:51
     */
    private void operationLog() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String ip = (String) requestContext.get(ZuulConstant.REQUEST_IP);
        String userAgent = (String) requestContext.get(ZuulConstant.REQUEST_USER_AGENT);
        String uri = (String) requestContext.get(ZuulConstant.REQUEST_URI);
        Map<String, String[]> paramMap = (Map<String, String[]>) requestContext.get(ZuulConstant.REQUEST_PARAM);
        long postMillis = System.currentTimeMillis() - (long) requestContext.get(ZuulConstant.REQUEST_TIME);
        RedisUser user = (RedisUser) requestContext.get(ZuulConstant.REQUEST_USER);

        Map<String, Object> map = new HashMap<>(8);
        map.put("remoteIp", ip);
        map.put("userAgent", userAgent);
        map.put("requestUri", uri);
        map.put("requestParam", GsonUtil.toJson(paramMap));
        map.put("source", "0");
        map.put("postTime", postMillis);
        if (null != user) {
            map.put("optAccount", user.getUsername());
            map.put("optName", user.getRealname());
        }
        this.ssoApiService.saveOperationLog(map);
    }
}
