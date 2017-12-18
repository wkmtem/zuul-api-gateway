package com.nsntc.zuul.constant;

/**
 * Class Name: ZuulConstant
 * Package: com.nsntc.zuul.constant
 * Description: zuul 常量
 * @author wkm
 * Create DateTime: 2017/12/18 上午2:09
 * Version: 1.0
 */
public interface ZuulConstant {

    String NEXT_FILTER = "next_filter";

    String THROWABLE_KEY = "throwable";

    /**
     * 忽略过滤地址前缀
     */
    String FILTER_IGNORE_PREFIX = "/sso/";

    String COOKIE_KEY = "SSO_TOKEN";

    String REQUEST_URI = "request_uri";

    String REQUEST_IP = "request_ip";

    String REQUEST_USER = "request_user";

    String REQUEST_TIME = "request_time";

    String REQUEST_PARAM = "request_param";

    String REQUEST_USER_AGENT = "request_user_agent";

}
