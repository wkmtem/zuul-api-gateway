package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.zuul.constant.ZuulConstant;
import org.springframework.stereotype.Component;

@Component
public class TestFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        boolean flag =  (boolean) requestContext.get(ZuulConstant.NEXT_FILTER);
        System.out.println("第二个过滤器状态: " + flag);
        return flag;
    }

    @Override
    public String filterType() {
        return "pre";
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

    {

}
}
