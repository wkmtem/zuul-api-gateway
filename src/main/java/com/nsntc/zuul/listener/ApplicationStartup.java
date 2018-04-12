package com.nsntc.zuul.listener;

import com.nsntc.commons.abs.AbstractAfterSpringContextStarted;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Class Name: ApplicationStartup
 * Package: com.nsntc.interview.listener
 * Description:
 * @author wkm
 * Create DateTime: 2018/2/12 上午2:38
 * Version: 1.0
 */
@SpringBootConfiguration
public class ApplicationStartup extends AbstractAfterSpringContextStarted {

    /**
     * Class Name: ApplicationStartup
     * Package: com.nsntc.zuul.listener
     * Description:
     * @author wkm
     * Create DateTime: 2018/4/12 下午4:21
     * Version: 1.0
     */
    @Override
    protected void applicationEvent(ContextRefreshedEvent event) {
        /** 初始化菜单权限 */
        //MenuPermissionUtils.initMenuPermission();
    }
}
