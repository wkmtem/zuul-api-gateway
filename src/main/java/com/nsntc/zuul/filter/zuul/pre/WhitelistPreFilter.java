package com.nsntc.zuul.filter.zuul.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.zuul.enums.ZuulFilterTypeEnum;
import com.nsntc.zuul.config.yml.GlobalYml;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.container.WhitelistContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

/**
 * Class Name: WhitelistPreFilter
 * Package: com.nsntc.zuul.filter
 * Description: 白名单前置过滤器
 * @author wkm
 * Create DateTime: 2017/12/16 上午8:02
 * Version: 1.0
 */
@Slf4j
@Component
public class WhitelistPreFilter extends ZuulFilter {

    @Autowired
    private GlobalYml globalYml;
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
}