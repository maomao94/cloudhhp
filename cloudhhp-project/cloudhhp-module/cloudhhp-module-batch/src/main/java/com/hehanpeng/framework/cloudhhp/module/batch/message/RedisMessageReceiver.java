package com.hehanpeng.framework.cloudhhp.module.batch.message;

import com.hehanpeng.framework.cloudhhp.common.exception.BatchMessageQueueException;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageQueueReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisMessageReceiver implements MessageQueueReceiver {

    private RedisTemplate redisTemplate;

    public void setRedis(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getMessage(String channel) {
        String message = null;
        try {
            message = (String) redisTemplate.opsForList().rightPop(channel);
        } catch (Exception e) {
            log.error("meesage queue error:{}", e);
            throw new BatchMessageQueueException(e.getMessage());
        }
        return message;
    }

}
