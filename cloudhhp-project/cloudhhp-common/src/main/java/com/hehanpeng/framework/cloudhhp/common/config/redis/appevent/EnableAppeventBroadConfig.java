package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description:
 * @date 2019/11/1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(value = {RedisDistributedEventBroadcaster.class})
public @interface EnableAppeventBroadConfig {
}
