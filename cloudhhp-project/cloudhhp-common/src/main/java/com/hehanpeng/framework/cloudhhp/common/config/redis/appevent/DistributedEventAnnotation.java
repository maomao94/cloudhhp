package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 上游渠道预处理类注解
 * Create on 2019/5/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DistributedEventAnnotation {

    String name();
}
