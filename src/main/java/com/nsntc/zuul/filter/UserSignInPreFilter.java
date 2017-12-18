package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.enums.ResultEnum;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.zuul.bean.RedisUser;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.service.microapi.SsoApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Class Name: UserSignInPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 用户登录前置过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Component
public class UserSignInPreFilter extends ZuulFilter {

    @Autowired
    private SsoApiService ssoApiService;

    /**
     * Method Name: shouldFilter
     * Description: 判断该过滤器是否需要执行: true: 执行, false: 不执行
     * Create DateTime: 2017/12/16 上午4:36
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return this.ignoreURI();
    }

    /**
     * Method Name: filterType
     * Description: 过滤器类型
     * Create DateTime: 2017/12/16 上午4:34
     * @return
     */
    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.PRE.getCode();
    }

    /**
     * Method Name: filterOrder
     * Description: 过滤器过滤链执行顺序, 数字越小优先级越高
     * Create DateTime: 2017/12/16 上午4:36
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * Method Name: run
     * Description: 过滤器具体业务逻辑
     * Create DateTime: 2017/12/16 上午4:35
     * @return
     */
    @Override
    public Object run() {
        this.checkUserToken();
        return null;
    }

    /**
     * Method Name: ignoreURI
     * Description: 是否忽略URI过滤
     * Create DateTime: 2017/12/18 上午12:31
     * @return
     */
    private boolean ignoreURI() {
        boolean flag = false;
        RequestContext requestContext = RequestContext.getCurrentContext();
        String uri = requestContext.getRequest().getRequestURI().toString();
        String userAgent = requestContext.getRequest().getHeader("user-agent");
        Map<String, String[]> parameterMap = requestContext.getRequest().getParameterMap();
        /** 非/sso/开头, 过滤 */
        if (!StringUtils.startsWithIgnoreCase(uri, ZuulConstant.FILTER_IGNORE_PREFIX)) {
            requestContext.set(ZuulConstant.REQUEST_URI, uri);
            requestContext.set(ZuulConstant.REQUEST_IP, RequestUtil.getRequest().getRemoteAddr());
            requestContext.set(ZuulConstant.REQUEST_USER_AGENT, userAgent);
            requestContext.set(ZuulConstant.REQUEST_TIME, System.currentTimeMillis());
            requestContext.set(ZuulConstant.REQUEST_PARAM, parameterMap);
            flag = true;
        }
        /** 向下传递"是否过滤" */
        requestContext.set(ZuulConstant.NEXT_FILTER, flag);
        return flag;
    }

    /**
     * Method Name: checkUserToken
     * Description: 校验用户token
     * Create DateTime: 2017/12/18 上午12:51
     */
    private void checkUserToken() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String cookieValue = RequestUtil.getCookieValue(ZuulConstant.COOKIE_KEY);
        /** redis微服务 */
        String jsonValue = this.ssoApiService.getUserByToken(cookieValue);

        /** 返回用户为空 */
        if (StringUtils.isEmpty(jsonValue)) {
            /** 拦截请求, 不对其进行路由 */
            requestContext.setSendZuulResponse(false);
            throw new ApplicationException(ResultEnum.USER_ACCOUNT_NOT_LOGIN);
        /** 容错机制 */
        } else if (StringUtils.equals(ResultEnum.SYSTEM_ERROR.getMessage(), jsonValue)) {
            /** 拦截请求, 不对其进行路由 */
            requestContext.setSendZuulResponse(false);
            throw new ApplicationException(ResultEnum.SYSTEM_ERROR);
        }

        try {
            requestContext.set(ZuulConstant.REQUEST_USER, GsonUtil.toObject(jsonValue, RedisUser.class));
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(ResultEnum.JSON_CONVERT_FAILURE.getMessage());
        }

        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
    }
}