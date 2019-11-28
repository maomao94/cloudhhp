package com.hehanpeng.framework.cloudhhp.module.transport.configuration;

import com.google.common.collect.Maps;
import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.module.transport.annotation.MqBusiHandler;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.MqBusiService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 * spring 上下文
 * 消息业务服务管理
 */

@Configuration
public class MqBusiServiceContainer implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private static Map<String, MqBusiService> MqBusiServiceMap = Maps.newHashMap();

    /**
     * 注册MqBusiService  通过注解获取transNetId作为key,将MqBusiService添加到map
     */
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MqBusiHandler.class);
        beans.forEach((s, serviceBean) -> {
            if (!(serviceBean instanceof MqBusiService)) {
                throw new Error("@MqBusiHandler should only be used on MqBusiService");
            }
            MqBusiHandler mqBusiHandler = serviceBean.getClass().getAnnotation(MqBusiHandler.class);
            MqBusiServiceMap.put(mqBusiHandler.transNetId(), (MqBusiService) serviceBean);
        });
    }

    /**
     * 获取消息处理服务
     *
     * @param transNetId 通道标识
     * @return 交易处理类
     */
    public static MqBusiService getMessageHandler(String transNetId) {
        return Optional.ofNullable(MqBusiServiceMap.get(transNetId)).orElseThrow(() -> new BizException("无消息处理服务"));
    }
}
