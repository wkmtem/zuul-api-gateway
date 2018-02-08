package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.interview.commons.utils.ResultUtil;
import com.nsntc.zuul.constant.ZuulConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * Class Name: ErrorZuulFilter
 * Package: com.nsntc.zuul.filter
 * Description: 异常过滤器
 * @author wkm
 * Create DateTime: 2017/12/18 下午4:45
 * Version: 1.0
 */
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
        this.handleException();
        return null;
    }

    /**
     * Method Name: handleException
     * Description: 异常处理
     * Create DateTime: 2017/12/18 上午4:08
     */
    private void handleException() {
        String responseBody;
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            /** 响应json编码 */
            requestContext.getResponse().setContentType(PartyTopConstant.RESPONSE_CONTENTTYPE_JSON);
            /** Http响应码 */
            requestContext.setResponseStatusCode(HttpServletResponse.SC_OK);

            Throwable exception = requestContext.getThrowable().getCause();
            if (exception instanceof ApplicationException) {
                ApplicationException e = (ApplicationException) exception;
                log.info("[Zuul异常过滤器] >>> {{}}, [错误信息] >>> {{}}", e.getCode(), e.getMessage());
                responseBody = GsonUtil.toJson(ResultUtil.error(e.getCode(), e.getMessage()));
                requestContext.setResponseBody(responseBody);
            }
            else if (exception instanceof HttpMessageNotReadableException) {
                HttpMessageNotReadableException e = (HttpMessageNotReadableException) exception;
                log.error("[Zuul异常过滤器] >>> {{}}", e.getMessage(), e);
                responseBody = GsonUtil.toJson(ResultUtil.error(ResultEnum.JSON_CONVERT_FAILURE));
                requestContext.setResponseBody(responseBody);
            }
            else {
                log.error("[Zuul异常过滤器] >>> {{}}", exception.getMessage(), exception);
                responseBody = GsonUtil.toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR));
                requestContext.setResponseBody(responseBody);
            }
            /** 移除 ZuulException */
            requestContext.remove(ZuulConstant.THROWABLE_KEY);
        } catch (Exception e) {
            log.error("[Zuul异常过滤器] >>> {{}}", e.getMessage(), e);
            rethrowRuntimeException(e);
        }
    }
}
