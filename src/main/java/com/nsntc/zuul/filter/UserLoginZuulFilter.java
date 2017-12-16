package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.constant.SystemConstant;
import com.nsntc.commons.enums.ResultEnum;
import com.nsntc.commons.utils.GsonUtil;
import com.nsntc.commons.utils.ResultUtil;
import com.nsntc.commons.utils.ServletRequestAttributesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Class Name: UserLoginZuulFilter
 * Package: com.nsntc.zuul.filter
 * Description: 用户登录过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Component
public class UserLoginZuulFilter extends ZuulFilter {

    private static final String FILTER_IGNORE_FIELD = "register";

    /**
     * Method Name: shouldFilter
     * Description: 判断该过滤器是否需要执行: true: 执行, false: 不执行
     * Create DateTime: 2017/12/16 上午4:36
     * @return
     */
    @Override
    public boolean shouldFilter() {

        String uri = ServletRequestAttributesUtil.getRequest().getRequestURI().toString();
        return !StringUtils.contains(uri, FILTER_IGNORE_FIELD);
    }

    /**
     * Method Name: filterType
     * Description: 返回过滤器类型
     *              pre: 请求在被路由之前执行
     *              routing: 在路由请求时调用
     *              error: 处理请求时发生错误调用
     *              post: 在routing和errror过滤器之后调用
     * Create DateTime: 2017/12/16 上午4:34
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
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

        RequestContext requestContext = RequestContext.getCurrentContext();
        String cookieValue = ServletRequestAttributesUtil.getCookieValue("SSO_TOKEN");
        System.out.println(cookieValue);
        /** 通过微服务 --> redis 查询登录情况 */
        String jsonValue = "";
        // todo 返回用户为空
        if (null == jsonValue || jsonValue == "") {
            /** 拦截请求, 不对其进行路由 */
            requestContext.setSendZuulResponse(false);
            requestContext.getResponse().setContentType(SystemConstant.RESPONSE_CONTENTTYPE_JSON);
            String responseBody = GsonUtil.toJson(ResultUtil.error(ResultEnum.USER_ACCOUNT_NOT_LOGIN));
            requestContext.setResponseBody(responseBody);
            return null;
        }
        return null;
    }
}