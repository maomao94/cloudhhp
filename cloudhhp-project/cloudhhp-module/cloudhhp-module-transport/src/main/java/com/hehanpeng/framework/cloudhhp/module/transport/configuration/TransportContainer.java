package com.hehanpeng.framework.cloudhhp.module.transport.configuration;

import com.google.common.collect.Maps;
import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.module.transport.annotation.TransportHandler;
import com.hehanpeng.framework.cloudhhp.module.transport.transport.Transport;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 * spring 上下文
 * 下游通讯处理服务管理
 */

@Configuration
public class TransportContainer implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private static Map<String, Transport> transportMap = Maps.newHashMap();

    /**
     * 注册MqBusiService  通过注解获取transNetId作为key,将Transport添加到map
     */
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(TransportHandler.class);
        beans.forEach((s, serviceBean) -> {
            if (!(serviceBean instanceof Transport)) {
                throw new Error("@TransportHandler should only be used on Transport");
            }
            TransportHandler transportHandler = serviceBean.getClass().getAnnotation(TransportHandler.class);
            transportMap.put(transportHandler.transNetId(), (Transport) serviceBean);
        });
    }

    /**
     * 获取下游通道通讯处理服务
     *
     * @param transNetId 通道标识
     * @return 下游通道通讯处理服务
     */
    public static Transport getMessageHandler(String transNetId) {
        return Optional.ofNullable(transportMap.get(transNetId)).orElseThrow(() -> new BizException("无下游通道通讯处理服务"));
    }
}
