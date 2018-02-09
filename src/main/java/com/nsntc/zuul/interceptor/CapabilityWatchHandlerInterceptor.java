package com.nsntc.zuul.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class Name: CapabilityWatchHandlerInterceptor
 * Package: com.nsntc.zuul.interceptor
 * Description: 性能监控拦截
 * @author wkm
 * Create DateTime: 2018/2/8 下午9:30
 * Version: 1.0
 */
@Slf4j
public class CapabilityWatchHandlerInterceptor implements HandlerInterceptor {

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        long beginTime = System.currentTimeMillis();
        startTimeThreadLocal.set(beginTime);
        /** 放行 */
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object handler, Exception e) throws Exception {
        long endTime = System.currentTimeMillis();
        long beginTime = startTimeThreadLocal.get();
        long consumeTime = endTime - beginTime;
        /** 处理时间超过500毫秒的请求为慢请求 */
        if (consumeTime > 400) {
            log.warn(String.format(">>> [%s] consume [%d] millis", httpServletRequest.getRequestURI(), consumeTime));
        }
    }
}
