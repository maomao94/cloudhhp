package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Slf4j
@Configuration
public class RedisMsgPubSubListener extends MessageListenerAdapter {
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired(required = false)
    private DistributedEventAnnotationContainer distributedEventAnnotationContainer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        DistributedEvent msg = (DistributedEvent) redisTemplate.getValueSerializer().deserialize(message.getBody());
        distributedEventAnnotationContainer.process(msg);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic(EventConstants.DISTRIBUTED_EVENT));
        return container;
    }
}