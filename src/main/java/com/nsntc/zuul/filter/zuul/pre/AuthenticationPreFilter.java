package com.nsntc.zuul.filter.zuul.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.commons.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.interview.commons.bean.CacheUser;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * Class Name: AuthenticationPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 鉴权过滤器
 * @author wkm
 * Create DateTime: 2017/12/19 上午2:37
 * Version: 1.0
 */
@Slf4j
@Component
public class AuthenticationPreFilter extends ZuulFilter {

    private List<String> uriList;
    private PathMatcher pathMatcher;

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return  (boolean) requestContext.get(ZuulConstant.NEXT_FILTER);
    }

    @Override
    public String filterType() {
        return ZuulFilterTypeEnum.PRE.getCode();
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public Object run() {

        boolean flag = false;

        RequestContext requestContext = RequestContext.getCurrentContext();
        String uri = requestContext.getRequest().getRequestURI().toString();

        CacheUser cacheUser = (CacheUser) requestContext.get(PartyTopConstant.CURRENT_USER);
        String[] urls = cacheUser.getUrls();
        if (null != urls) {
            uriList = Arrays.asList(urls);
            pathMatcher = new AntPathMatcher();
            flag = this.matchPermissionUri(uri);
        }
        if (!flag) {
            log.info("[Zuul鉴权过滤器] >>> [用户'{}' 无权访问'{}']", cacheUser.getUsername(), uri);
            throw new ApplicationException(ResultEnum.UNAUTHORIZED_ACCESS);
        }
        /** 放行请求, 对其进行路由 */
        requestContext.setSendZuulResponse(true);
        return null;
    }

    /**
     * Method Name: matchPermissionUri
     * Description: 校验权限
     * Create DateTime: 2018/2/11 下午9:20
     * @param uri
     * @return
     */
    private boolean matchPermissionUri(String uri) {
        for (String u : this.uriList) {
            if (this.pathMatcher.match(u, uri)) {
                return true;
            }
        }
        return false;
    }
}
