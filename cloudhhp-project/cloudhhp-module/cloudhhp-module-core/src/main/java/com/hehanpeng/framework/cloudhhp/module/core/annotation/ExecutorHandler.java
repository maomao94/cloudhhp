package com.hehanpeng.framework.cloudhhp.module.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)//对类或接口等的注解
@Retention(RetentionPolicy.RUNTIME) //运行时注解依然会存在
@Documented
@Component
public @interface ExecutorHandler {
}
