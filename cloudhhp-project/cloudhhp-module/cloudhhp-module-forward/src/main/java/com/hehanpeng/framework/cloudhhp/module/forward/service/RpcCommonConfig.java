package com.hehanpeng.framework.cloudhhp.module.forward.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.HelloDubboService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created with Intellij IDEA 2019.2
 *
 * @author: hehanpeng
 * Email: 287737281@qq.com
 * 2019/9/10 15:30
 */
@Configuration
public class RpcCommonConfig {

    @Reference(version = "${demo.service.version}", timeout = 10000, retries = 0, check = false,async = true)
    public HelloDubboService helloDubboService;

    @Bean
    public Object setHelloDubboService() {
        return helloDubboService;
    }
}
