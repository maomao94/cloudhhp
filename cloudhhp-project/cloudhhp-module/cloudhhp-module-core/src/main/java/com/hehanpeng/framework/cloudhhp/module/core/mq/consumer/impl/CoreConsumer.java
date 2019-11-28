package com.hehanpeng.framework.cloudhhp.module.core.mq.consumer.impl;

import com.alibaba.fastjson.JSONObject;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.mq.handler.CoreHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hehanpeng
 * 2019/4/29 20:30
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "${core.rocketmq.coreReqTopic}", consumerGroup = "${core.rocketmq.coreConsumerGroup}")
public class CoreConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Autowired
    CoreHandler coreHandler;

    @Override
    public void onMessage(MessageExt message) {
        try {
            Map<String, String> messageHeaders = message.getProperties();
            MqMessage msg = JSONObject.parseObject(new String(message.getBody()), MqMessage.class);
            long currentTime = System.currentTimeMillis();
            log.info("logId={},position=RecvReq,transnetId={},bizType={},currentTime={},appId:{},msg:{},received gate message",
                    msg.getLogId(), msg.getTransnetId(), msg.getBizType(), currentTime,
                    messageHeaders.get("appId"), msg.toString());
            coreHandler.handler(msg);
        } catch (Exception e) {
            log.error("core CoreConsumer error", e);
        }
        log.info("core CoreConsumer received message end");
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
        consumer.setMaxReconsumeTimes(0);
    }
}
