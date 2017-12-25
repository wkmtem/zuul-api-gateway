package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class Name: OperationLogPostFilter
 * Package: com.nsntc.zuul.filter
 * Description: 操作日志后置过滤器
 * @author wkm
 * Create DateTime: 2017/12/18 下午10:15
 * Version: 1.0
 */
@Component
public class OperationLogPostFilter extends ZuulFilter {

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
        return null;
    }
}
