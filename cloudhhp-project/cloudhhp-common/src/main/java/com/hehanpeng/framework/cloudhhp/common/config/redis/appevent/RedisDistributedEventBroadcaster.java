package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis发布订阅消息监听器
 */
@Slf4j
@Configuration
public class RedisDistributedEventBroadcaster implements DistributedEventBroadcaster {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void publishEvent(DistributedEvent distributedEvent) {
        if (distributedEvent.getEventType() == null) {
            throw new RuntimeException("The distributedEvent appEventType is required.");
        }

        if (distributedEvent.getKey() == null) {
            throw new RuntimeException("The distributedEvent key is required.");
        }

        log.info("eventType={},key={},distributedEvent DISTRIBUTED_EVENT", distributedEvent.getEventType(), distributedEvent.getKey());
        redisTemplate.convertAndSend(EventConstants.DISTRIBUTED_EVENT, distributedEvent);
    }
}


