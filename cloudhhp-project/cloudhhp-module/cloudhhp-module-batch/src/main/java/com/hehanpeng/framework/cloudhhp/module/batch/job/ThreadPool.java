package com.hehanpeng.framework.cloudhhp.module.batch.job;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPool extends ThreadPoolTaskExecutor {

    public ThreadPool() {
        this.setCorePoolSize(16);
        this.setMaxPoolSize(16);
        this.setAllowCoreThreadTimeOut(true);
        this.afterPropertiesSet();
    }

}
