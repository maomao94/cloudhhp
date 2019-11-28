package com.hehanpeng.framework.cloudhhp.module.gate.mq.producer.impl;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.gate.mq.producer.AbstractMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hehanpeng
 * 2019/4/29 20:30
 */
@Service
@Slf4j
public class RocketMq2CoreProducer extends AbstractMqProducer<MqMessage> {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Value("${core.rocketmq.coreReqTopic}")
    private String coreReqTopic;

    @Value("${core.rocketmq.coreTag}")
    private String coreTag;

    @Override
    public void asyncSend(MqMessage msg) throws Exception {
        asyncSend(msg, coreReqTopic, coreTag);
    }

    @Override
    public void asyncSend(MqMessage msg, String topic, String flag) throws Exception {
        asyncSendMessage(msg, topic, flag);
    }

    @Override
    public void syncSend(MqMessage msg) throws Exception {

    }

    @Override
    public void syncSend(MqMessage msg, String topic, String flag) throws Exception {

    }

    @Override
    public void syncSend(MqMessage msg, int delay) throws Exception {

    }

    @Override
    public void syncSend(MqMessage msg, int delay, String topic, String flag) throws Exception {

    }

    @Override
    public void doAfterSyncSend(MqMessage msg, SendResult sendResult) throws Exception {

    }

    private void asyncSendMessage(MqMessage msg, String topic, String flag) throws Exception {
        Map<String, Object> msgHead = new HashMap<>();
        msgHead.put("appId", "gate");
        log.info("logId={},position=SendRsp,bizType={},transnetId={},currentTime={},msg:{},send core message", msg.getLogId(),
                msg.getBizType(), msg.getTransnetId(), System.currentTimeMillis(), msg.toString());
        rocketMQTemplate.asyncSend(topic + ":" + flag, MessageBuilder.createMessage(msg, new MessageHeaders(msgHead)), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("gate send core success: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("gate send core error", throwable);
            }
        });
    }
}
