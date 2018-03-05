package com.nsntc.zuul.config.session;

import com.nsntc.zuul.config.yml.SessionYml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.annotation.PostConstruct;

/**
 * Class Name: RedisSessionConfig
 * Package: com.nsntc.interview.config.session
 * Description: 
 * @author wkm
 * Create DateTime: 2018/2/11 下午5:53
 * Version: 1.0
 */
@SpringBootConfiguration
/** maxInactiveIntervalInSeconds: 默认1800秒,原server.session.timeout属性不再生效 */
@EnableRedisHttpSession
public class RedisSessionConfig {

    @Autowired
    private SessionYml sessionYml;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    /**
     * Method Name: cookieHttpSessionStrategy
     * Description: 向游览器写入cookie，默认的cookiename是SESSION
     * Create DateTime: 2018/2/11 下午7:56
     * @return
     */
    /*@Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy(){
        CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName(this.sessionYml.getCookieName());
        strategy.setCookieSerializer(cookieSerializer);
        return strategy;
    }*/

    /**
     * Method Name: afterPropertiesSet
     * Description: redis中session参数
     * Create DateTime: 2018/2/11 下午7:50
     */
    @PostConstruct
    private void afterPropertiesSet() {
        sessionRepository.setDefaultMaxInactiveInterval(this.sessionYml.getMaxInactiveIntervalInSeconds());
        sessionRepository.setRedisKeyNamespace(this.sessionYml.getRedisNamespace());
    }
}
