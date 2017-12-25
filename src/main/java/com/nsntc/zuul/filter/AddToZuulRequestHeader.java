package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.constant.SystemConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Class Name: AddToZuulRequestHeader
 * Package: com.nsntc.zuul.filter
 * Description:
 * @author wkm
 * Create DateTime: 2017/12/25 上午7:28
 * Version: 1.0
 */
@Component
@Slf4j
public class AddToZuulRequestHeader extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.PRE.getCode();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() {
        this.setRemoteAddrToZuulRequestHeaders();
        return null;
    }

    /**
     * Method Name: setRemoteAddrToZuulRequestHeaders
     * Description: 设置请求ip
     * Create DateTime: 2017/12/25 上午6:16
     */
    private void setRemoteAddrToZuulRequestHeaders() {
        RequestContext requestContext = RequestContext.getCurrentContext();

        /** Rely on HttpServletRequest to retrieve the correct remote address from upstream X-Forwarded-For header */
        HttpServletRequest request = requestContext.getRequest();
        String remoteAddr = request.getRemoteAddr();

        /** Pass remote address downstream by setting X-Forwarded for header again on Zuul request */
        log.info("[Remote Addr] >>> {{}}", remoteAddr);
        requestContext.getZuulRequestHeaders().put(SystemConstant.ZUUL_REMOTE_ADDR, remoteAddr);
    }
}
