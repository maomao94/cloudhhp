package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 上游渠道预处理工厂类
 */
@Slf4j
@Configuration
public class DistributedEventAnnotationContainer implements ApplicationContextAware, SmartInitializingSingleton {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private static Map<String, DistributedEventSubscriber> distributedEventSubscriberHashMap = new HashMap<>();

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = context.getBeansWithAnnotation(DistributedEventAnnotation.class);
        beans.forEach((s, serviceBean) -> {
            if (!(serviceBean instanceof DistributedEventSubscriber)) {
                throw new Error("@DistributedEventAnnotation should only be used on DistributedEventSubscriber");
            }
            DistributedEventAnnotation distributedEventAnnotation = serviceBean.getClass().getAnnotation(DistributedEventAnnotation.class);
            distributedEventSubscriberHashMap.put(distributedEventAnnotation.name(), (DistributedEventSubscriber) serviceBean);
        });
    }

    public void process(DistributedEvent distributedEvent) throws BizException {
        log.info("distributedEvent={}", distributedEvent);
        DistributedEventSubscriber distributedEventSubscriber = distributedEventSubscriberHashMap.get(distributedEvent.getEventType());
        if (null != distributedEventSubscriber) {
            distributedEventSubscriber.notifyEvent(distributedEvent);
        }
    }
}
