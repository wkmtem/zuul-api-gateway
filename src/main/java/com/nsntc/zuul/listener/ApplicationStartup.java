package com.nsntc.zuul.listener;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationListener;
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
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * ContextRefreshedEvent 当ApplicationContext或spring被初始化或者刷新initialized会触发该事件
     * ContextStartedEvent	 spring初始化完时触发
     * ContextStoppedEvent	 spring停止后触发，一个停止了的动作，可以通过start()方法从新启动
     * ContextClosedEvent	 spring关闭，所有bean都被destroyed掉了,这个时候不能被刷新，或者从新启动了
     * RequestHandledEvent	 请求经过DispatcherServlet时被触发，在request完成之后
     * ApplicationReadyEvent 当spring mvc application应该初始化完成，可以准备接收请求
     * StatusChangeEvent netflix 事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /**
         * spring boot 默认情况下只有一个 WebApplicationContext
         * org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
         * */
        if (event.getApplicationContext() instanceof AnnotationConfigEmbeddedWebApplicationContext) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
            /** 初始化菜单权限 */
            //MenuPermissionUtils.initMenuPermission();
        }
    }
}
