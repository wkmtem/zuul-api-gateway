package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import org.springframework.stereotype.Component;

/**
 * Class Name: AuthenticationPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 鉴权过滤器
 * @author wkm
 * Create DateTime: 2017/12/19 上午2:37
 * Version: 1.0
 */
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
        return 1;
    }

    @Override
    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
        System.out.println("第二个过滤器拦截成功");
        return null;
    }
}
