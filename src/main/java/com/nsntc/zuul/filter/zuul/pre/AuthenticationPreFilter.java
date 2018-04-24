package com.nsntc.zuul.filter.zuul.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nsntc.commons.bean.SessionPermission;
import com.nsntc.commons.bean.SessionUser;
import com.nsntc.commons.constant.PartyTopConstant;
import com.nsntc.zuul.enums.ZuulFilterTypeEnum;
import com.nsntc.commons.exception.ApplicationException;
import com.nsntc.commons.utils.RequestUtil;
import com.nsntc.commons.utils.SessionUtil;
import com.nsntc.interview.commons.bean.CacheUser;
import com.nsntc.interview.commons.enums.ResultEnum;
import com.nsntc.zuul.constant.ZuulConstant;
import com.nsntc.zuul.container.GeneralPermissionsContainer;
import com.nsntc.zuul.container.NewestPermissionTokenContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private GeneralPermissionsContainer generalPermissionsContainer;

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
        /** 初始化通用权限 */
        this.generalPermissionsContainer.initGeneralPermission();

        if (!GeneralPermissionsContainer.getGeneralPermissionList().contains(uri)) {
            // todo 根据用户token 获取菜单权限
            if (!this.isPermitted(uri)) {
                log.info("[Zuul鉴权过滤器] >>> [用户'{}' 无权访问'{}']", cacheUser.getUsername(), uri);
                throw new ApplicationException(ResultEnum.UNAUTHORIZED_ACCESS);
            }
            /*String[] urls = cacheUser.getUrls();
            if (null != urls) {
                uriList = Arrays.asList(urls);
                pathMatcher = new AntPathMatcher();
                flag = this.matchPermissionUri(uri);
            }
            if (!flag) {
                log.info("[Zuul鉴权过滤器] >>> [用户'{}' 无权访问'{}']", cacheUser.getUsername(), uri);
                throw new ApplicationException(ResultEnum.UNAUTHORIZED_ACCESS);
            }*/
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

    /**
     * Method Name: isPermitted
     * Description: 是否允许访问
     * Create DateTime: 2018/3/9 下午2:44
     * @param uri
     * @return
     */
    private boolean isPermitted(String uri) {
        Set<String> permissionUrlSet = this.getPermissionFromSession();
        return permissionUrlSet.contains(uri);
    }

    /**
     * Method Name: getPermissionSetFromSession
     * Description: 从session获取当前线程用户权限set
     * Create DateTime: 2018/3/5 下午3:58
     * @return
     */
    private Set<String> getPermissionFromSession() {
        HttpServletRequest request = RequestUtil.getRequest();
        SessionPermission sessionPermission = SessionUtil.getSessionPermission(request);
        if (sessionPermission == null || this.sessionUserPermissionChanged()) {
            sessionPermission = this.setPermissionToSession();
        }
        return sessionPermission.getPermissionSet();
    }

    /**
     * Method Name: sessionUserPermissionChanged
     * Description: session用户权限是否改变
     * Create DateTime: 2018/3/7 下午6:30
     * @return
     */
    private boolean sessionUserPermissionChanged() {
        HttpServletRequest request = RequestUtil.getRequest();
        SessionUser sessionUser = SessionUtil.getSessionUser(request);
        /** true && true == true */
        return NewestPermissionTokenContainer.isChanged && !NewestPermissionTokenContainer.tokenSet.contains(sessionUser.getToken());
    }

    /**
     * Method Name: setPermissionToSession
     * Description: 设置权限到session中
     * Create DateTime: 2018/3/7 下午6:45
     * @return
     */
    private SessionPermission setPermissionToSession() {
        HttpServletRequest request = RequestUtil.getRequest();
        SessionUser sessionUser = SessionUtil.getSessionUser(request);
        // todo 根据用户tonken获取菜单权限
        sessionUser.getToken();


        SessionPermission sessionPermission = new SessionPermission();
        /** 菜单 */
        //sessionPermission.setMenuList(menuList);
        /** 权限 */
        //sessionPermission.setPermissionSet(operateSet);
        /** 没有的权限URL，方便前端去隐藏相应操作按钮 */
        Set<String> noPermissionSet = new HashSet<>();
        //noPermissionSet.removeAll(operateSet);
        sessionPermission.setNoPermissions(StringUtils.arrayToDelimitedString(noPermissionSet.toArray(), ","));

        /** 设置权限到session中 */
        SessionUtil.setSessionPermission(request, sessionPermission);

        /** 当前session用户已更新最新权限，添加权限监控集合 */
        if (NewestPermissionTokenContainer.isChanged) {
            NewestPermissionTokenContainer.tokenSet.add(sessionUser.getToken());
        }

        // todo 微服务调用sso 设置cacheUser到redis中
        RequestContext requestContext = RequestContext.getCurrentContext();
        CacheUser cacheUser = (CacheUser) requestContext.get(PartyTopConstant.CURRENT_USER);
        //cacheUser.setRoleIds();
        //cacheUser.setMenuList();
        //cacheUser.setUrls();
        //cacheUser.setButtonIds();
        return sessionPermission;
    }
}
