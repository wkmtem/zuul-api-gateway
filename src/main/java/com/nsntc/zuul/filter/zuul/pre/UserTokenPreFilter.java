package com.nsntc.zuul.filter.zuul.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.bean.Result;
import com.nsntc.commons.bean.SessionUser;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.zuul.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.JsonUtil;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.commons.utils.SessionUtil;
import com.nsntc.interview.commons.bean.CacheUser;
import com.nsntc.interview.commons.constant.CookieConstant;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.micro.consumer.sso.SsoApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Class Name: UserTokenPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 用户token前置过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Slf4j
@Component
public class UserTokenPreFilter extends ZuulFilter {

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
        return  (boolean) requestContext.get(ZuulConstant.NEXT_FILTER);
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
        return 2;
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
     * Method Name: checkUserToken
     * Description: 校验用户token
     * Create DateTime: 2017/12/18 上午12:51
     */
    private void checkUserToken() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        String cookieValue = RequestUtil.getCookieValue(CookieConstant.COOKIE_KEY);

        /** cookie不存在 */
        if (StringUtils.isEmpty(cookieValue)) {
            log.info("[Zuul登录过滤器] >>> [COOKIE不存在]");
            throw new ApplicationException(ResultEnum.COOKIE_NOT_EXIST);
        }
        // todo 根据token查询用户
        CacheUser cacheUser = this.getUserByToken(cookieValue);
        requestContext.set(PartyTopConstant.CURRENT_USER, cacheUser);

        /** 写入session */
        this.setUserToSession(cookieValue, cacheUser.getUsername());

        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
    }

    /**
     * Method Name: getUserByToken
     * Description: 根据token获取用户
     * Create DateTime: 2018/3/5 下午5:21
     * @param token
     * @return
     */
    private CacheUser getUserByToken(String token) {
        /** sso微服务 */
        Result result = this.ssoApiService.getUserByToken(token);
        if (!ResultEnum.OK.getCode().equals(result.getCode())) {
            log.error("[Zuul登录过滤器] >>> {{}}, [错误信息] >>> {{}}", result.getCode(), result.getMsg());
            throw new ApplicationException(result.getCode(), result.getMsg());
        }
        /** 未登录 */
        if (null == result.getData()) {
            log.info("[Zuul登录过滤器] >>> [用户未登录]");
            throw new ApplicationException(ResultEnum.USER_ACCOUNT_NOT_LOGIN);
        }
        return JsonUtil.jsonToObject((String) result.getData(), CacheUser.class);
    }

    /**
     * Method Name: setUserToSession
     * Description: 设置用户到session中
     * Create DateTime: 2018/3/5 下午3:58
     * @return
     */
    private void setUserToSession(String token, String username) {
        HttpServletRequest request = RequestUtil.getRequest();
        SessionUser sessionUser = SessionUtil.getSessionUser(request);
        if (null == sessionUser) {
            sessionUser = new SessionUser(token, username);
            SessionUtil.setSessionUser(request, sessionUser);
        }
    }
}