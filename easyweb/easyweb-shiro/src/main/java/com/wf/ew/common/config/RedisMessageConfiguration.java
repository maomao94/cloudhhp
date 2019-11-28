package com.wf.ew.common.config;

import com.wf.ew.common.message.MessageSender;
import com.wf.ew.common.message.RedisMessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisMessageConfiguration {

    @Bean
    public MessageSender redisMessageSender() {
        return new RedisMessageSender();
    }
}
