package com.nsntc.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.bean.Result;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.JsonUtil;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.interview.commons.bean.CacheUser;
import com.nsntc.interview.commons.constant.CookieConstant;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.config.yml.GlobalYml;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.container.WhitelistContainer;
import com.nsntc.zuul.micro.consumer.sso.SsoApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

/**
 * Class Name: UserSignInPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 用户登录前置过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Slf4j
@Component
public class UserSignInPreFilter extends ZuulFilter {

    @Autowired
    private GlobalYml globalYml;
    @Autowired
    private SsoApiService ssoApiService;
    @Autowired
    private WhitelistContainer whitelistContainer;

    private PathMatcher pathMatcher;

    /**
     * Method Name: shouldFilter
     * Description: 判断该过滤器是否需要执行: true: 执行, false: 不执行
     * Create DateTime: 2017/12/16 上午4:36
     * @return
     */
    @Override
    public boolean shouldFilter() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        if (this.globalYml.getSwitchVal()) {
            return this.ignoreWhitelistURI(requestContext);
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
     * Method Name: ignoreWhitelistURI
     * Description: 是否忽略白名单URI
     * Create DateTime: 2017/12/30 下午5:54
     * @param requestContext
     * @return
     */
    private boolean ignoreWhitelistURI(RequestContext requestContext) {

        boolean flag = true;
        this.whitelistContainer.initWhitelist();
        String uri = requestContext.getRequest().getRequestURI().toString();
        if (!CollectionUtils.isEmpty(WhitelistContainer.getWhitelist())) {
            this.pathMatcher = new AntPathMatcher();
            flag = !this.matchWhitelistPath(uri);
        }
        /** 向下传递"是否过滤" */
        requestContext.set(ZuulConstant.NEXT_FILTER, flag);
        return flag;
    }

    /**
     * Method Name: matchWhitelistPath
     * Description: 校验白名单
     * Create DateTime: 2018/2/11 下午9:20
     * @param uri
     * @return
     */
    private boolean matchWhitelistPath(String uri) {
        for (String white : WhitelistContainer.getWhitelist()) {
            if (this.pathMatcher.match(white, uri)) {
                return true;
            }
        }
        return false;
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
        /** sso微服务 */
        Result result = this.ssoApiService.getUserByToken(cookieValue);
        if (!ResultEnum.OK.getCode().equals(result.getCode())) {
            log.error("[Zuul登录过滤器] >>> {{}}, [错误信息] >>> {{}}", result.getCode(), result.getMsg());
            throw new ApplicationException(result.getCode(), result.getMsg());
        }
        /** 未登录 */
        if (null == result.getData()) {
            log.info("[Zuul登录过滤器] >>> [用户未登录]");
            throw new ApplicationException(ResultEnum.USER_ACCOUNT_NOT_LOGIN);
        }
        requestContext.set(PartyTopConstant.CURRENT_USER, JsonUtil.jsonToObject((String) result.getData(), CacheUser.class));
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
    }
}