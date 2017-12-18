package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.enums.ResultEnum;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.zuul.constant.ZuulConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Class Name: UserSignInZuulFilter
 * Package: com.nsntc.zuul.filter
 * Description: 用户登录过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Component
public class UserSignInZuulFilter extends ZuulFilter {

    private static final String FILTER_IGNORE_PREFIX = "/sso/";

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
        boolean flag = true;
        String uri = RequestUtil.getRequest().getRequestURI().toString();
        /** /sso/开头, 则不过滤 */
        if (StringUtils.startsWithIgnoreCase(uri, FILTER_IGNORE_PREFIX)) {
            flag = false;
        }
        RequestContext requestContext = RequestContext.getCurrentContext();
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
        String cookieValue = RequestUtil.getCookieValue("SSO_TOKEN");
        System.out.println(cookieValue);
        /** 通过微服务 --> redis 查询登录情况 */
        String jsonValue = "";
        // todo 返回用户为空
        if (StringUtils.isEmpty(jsonValue)) {
            /** 拦截请求, 不对其进行路由 */
            requestContext.setSendZuulResponse(false);
            throw new ApplicationException(ResultEnum.USER_ACCOUNT_NOT_LOGIN);
        }
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
    }
}