package com.hehanpeng.framework.cloudhhp.module.core.configuration;

import com.hehanpeng.framework.cloudhhp.module.core.annotation.ExecutorHandler;
import com.hehanpeng.framework.cloudhhp.module.core.event.DefaultEventManager;
import com.hehanpeng.framework.cloudhhp.module.core.event.Event;
import com.hehanpeng.framework.cloudhhp.module.core.event.EventHandler;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * spring 上下文
 * 异步线程池事件容器
 */

@Configuration
public class ExecutorEventContainer implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    private DefaultEventManager defaultEventManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setDefaultEventManager(DefaultEventManager defaultEventManager) {
        this.defaultEventManager = defaultEventManager;
    }

    /**
     * 注册ExecutorHandler
     */
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ExecutorHandler.class);
        beans.forEach((s, serviceBean) -> {
            if (!(serviceBean instanceof EventHandler)) {
                throw new Error("@ExecutorHandler should only be used on EventHandler");
            }
            ExecutorHandler transportHandler = serviceBean.getClass().getAnnotation(ExecutorHandler.class);
            //xml注入改为注解驱动
            defaultEventManager.register((EventHandler<Event>) serviceBean);
        });
    }
}
