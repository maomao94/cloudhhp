package com.hehanpeng.framework.cloudhhp.module.transport.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 通道通讯处理注解
 */
@Target(ElementType.TYPE)//对类或接口等的注解
@Retention(RetentionPolicy.RUNTIME) //运行时注解依然会存在
@Documented
@Component
public @interface TransportHandler {
    /**
     * 通道标识
     *
     * @return
     */
    String transNetId();
}
