package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.interview.commons.bean.RedisUser;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Class Name: AuthenticationPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 鉴权过滤器
 * @author wkm
 * Create DateTime: 2017/12/19 上午2:37
 * Version: 1.0
 */
@Slf4j
@Component
public class AuthenticationPreFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return  (boolean) requestContext.get(ZuulConstant.NEXT_FILTER);
    }

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.PRE.getCode();
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        String uri = requestContext.getRequest().getRequestURI().toString();

        RedisUser redisUser = (RedisUser) requestContext.get(PartyTopConstant.CURRENT_USER);
        String[] urls = redisUser.getUrls();
        if (!Arrays.asList(urls).contains(uri)) {
            log.info("[Zuul鉴权过滤器] >>> [用户'{}' 无权访问'{}']", redisUser.getUsername(), uri);
            throw new ApplicationException(ResultEnum.UNAUTHORIZED_ACCESS);
        }
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
        return null;
    }
}
