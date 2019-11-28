package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

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
@EnableAppeventBroadConfig
public @interface EnableAppeventBroad {
}
