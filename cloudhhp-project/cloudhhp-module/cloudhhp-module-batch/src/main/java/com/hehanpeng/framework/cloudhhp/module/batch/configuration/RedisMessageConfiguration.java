package com.hehanpeng.framework.cloudhhp.module.batch.configuration;

import com.hehanpeng.framework.cloudhhp.module.batch.message.MessageListenerInitilization;
import com.hehanpeng.framework.cloudhhp.module.batch.message.RedisMessageReceiver;
import com.hehanpeng.framework.cloudhhp.module.batch.message.RedisMessageSender;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageQueueReceiver;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisMessageConfiguration {

    @Bean
    public MessageListenerInitilization messageInitilization(RedisTemplate redisTemplate) {
        MessageListenerInitilization init = new MessageListenerInitilization();
        init.setQueue(redisMessageQueue(redisTemplate));
        return init;
    }

    @Bean
    public MessageSender redisMessageSender() {
        return new RedisMessageSender();
    }

    public MessageQueueReceiver redisMessageQueue(RedisTemplate redisTemplate) {
        RedisMessageReceiver receiver = new RedisMessageReceiver();
        receiver.setRedis(redisTemplate);
        return receiver;
    }
}
