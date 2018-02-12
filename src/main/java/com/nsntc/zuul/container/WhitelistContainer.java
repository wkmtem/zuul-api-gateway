package com.nsntc.zuul.container;

import com.nsntc.zuul.config.yml.GlobalYml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Class Name: WhitelistContainer
 * Package: com.nsntc.zuul.container
 * Description: 白名单容器
 * @author wkm
 * Create DateTime: 2018/2/12 上午6:33
 * Version: 1.0
 */
@Component
public class WhitelistContainer {

    @Autowired
    private GlobalYml globalYml;

    /**
     * 白名单
     */
    private static List<String> whitelist;

    /**
     * 并发同步锁
     */
    private static Object monitor = new Object();

    /**
     * Method Name: initWhitelist
     * Description: 初始化白名单
     * Create DateTime: 2018/2/12 上午6:32
     */
    public void initWhitelist() {
        synchronized (monitor) {
            if (StringUtils.isNotEmpty(this.globalYml.getWhitelist())) {
                String[] excludes = StringUtils.split(this.globalYml.getWhitelist(), ',');
                for (int i = 0, len = excludes.length; i < len; i++) {
                    excludes[i] = StringUtils.trim(excludes[i]);
                }
                whitelist = Arrays.asList(excludes);
            }
        }
    }

    public static List<String> getWhitelist() {
        synchronized (monitor) {
            return whitelist;
        }
    }
}
