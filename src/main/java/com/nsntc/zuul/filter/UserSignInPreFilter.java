package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.bean.Result;
import com.nsntc.commons.constant.SystemConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.JsonUtil;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.interview.commons.bean.RedisUser;
import com.nsntc.interview.commons.constant.CookieConstant;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.config.yml.GlobalYml;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.micro.consumer.sso.SsoApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private GlobalYml globalYml;
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

        RequestContext requestContext = RequestContext.getCurrentContext();
        if (globalYml.getSwitchVal()) {
            return this.ignoreURI(requestContext);
        }
        /** 向下传递"是否过滤" */
        requestContext.set(ZuulConstant.NEXT_FILTER, false);
        return false;

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
        return 1;
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
     * Create DateTime: 2017/12/30 下午5:54
     * @param requestContext
     * @return
     */
    private boolean ignoreURI(RequestContext requestContext) {

        boolean flag = true;
        String uri = requestContext.getRequest().getRequestURI().toString();
        if (StringUtils.isNotEmpty(this.globalYml.getWhitelistPrefix())) {
            String[] filterIgnorePrefixs = StringUtils.split(this.globalYml.getWhitelistPrefix(), ',');
            /** 非/sso/开头, 过滤 */
            for (String filterIgnorePrefix : filterIgnorePrefixs) {
                if (StringUtils.startsWithIgnoreCase(uri, StringUtils.trim(filterIgnorePrefix))) {
                    flag = false;
                    break;
                }
            }
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
        String cookieValue = RequestUtil.getCookieValue(CookieConstant.COOKIE_KEY);
        /** cookie不存在 */
        if (StringUtils.isEmpty(cookieValue)) {
            throw new ApplicationException(ResultEnum.COOKIE_NOT_EXIST);
        }
        /** sso微服务 */
        Result result = this.ssoApiService.getUserByToken(cookieValue);
        if (!ResultEnum.OK.getCode().equals(result.getCode())) {
            throw new ApplicationException(result.getCode(), result.getMsg());
        }

        /** 未登录 */
        if (null == result.getData()) {
            throw new ApplicationException(ResultEnum.USER_ACCOUNT_NOT_LOGIN);
        }
        requestContext.set(SystemConstant.CURRENT_USER, JsonUtil.jsonToObjectList((String) result.getData(), RedisUser.class));
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
    }
}