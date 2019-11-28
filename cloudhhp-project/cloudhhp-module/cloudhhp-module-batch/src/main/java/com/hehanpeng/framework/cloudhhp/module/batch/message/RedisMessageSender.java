package com.hehanpeng.framework.cloudhhp.module.batch.message;

import com.alibaba.fastjson.JSON;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
public class RedisMessageSender implements MessageSender {
    @Autowired
    private RedisTemplate redisTemplate;

    public void sendMessage(String channel, Object content) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {

            @Override
            public void run() {
                Message message = new Message(content);
                message.setMsgid(UUID.randomUUID().toString());
                log.info("发送消息...channel:{},内容:{}", channel, JSON.toJSONString(message));
                try {
                    redisTemplate.opsForList().leftPush(channel.trim(), JSON.toJSONString(message));
                } catch (Exception e) {
                    log.error("redis send message error", e);
                }
            }
        });
    }
}
