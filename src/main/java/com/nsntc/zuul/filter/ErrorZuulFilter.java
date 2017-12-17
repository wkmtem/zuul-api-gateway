package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.nsntc.commons.constant.SystemConstant;
import com.nsntc.commons.enums.ResultEnum;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.commons.utils.ResultUtil;
import com.nsntc.zuul.constant.ZuulConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

@Component
@Slf4j
public class ErrorZuulFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.ERROR.getCode();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() {

        String responseBody = null;

        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            /** 响应json编码 */
            requestContext.getResponse().setContentType(SystemConstant.RESPONSE_CONTENTTYPE_JSON);
            /** Http响应码 */
            requestContext.setResponseStatusCode(HttpServletResponse.SC_OK);

            Exception exception = (Exception) requestContext.getThrowable().getCause();
            if (exception instanceof ApplicationException) {
                ApplicationException e = (ApplicationException) exception;
                log.error("[错误代码] >>> {{}}, [错误信息] >>> {{}}", e.getCode(), e.getMessage());
                responseBody = GsonUtil.toJson(ResultUtil.error(e.getCode(), e.getMessage()));
                requestContext.setResponseBody(responseBody);
            }
            else {
                log.error("[过滤器错误警告] >>> {{}}", exception.getMessage(), exception);
                responseBody = GsonUtil.toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR));
                requestContext.setResponseBody(responseBody);
            }
            /** 移除 ZuulException */
            requestContext.remove(ZuulConstant.THROWABLE_KEY);
        } catch (Exception e) {
            log.error("[未定义错误] >>> {{}}", e.getMessage(), e);
            rethrowRuntimeException(e);
        }
        return null;
    }
}
