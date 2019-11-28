package com.hehanpeng.framework.cloudhhp.module.transport.mq.producer;

import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author hehanpeng
 * 2019/4/16 16:47
 */
public abstract class AbstractMqProducer<T> {
    public abstract void asyncSend(T msg) throws Exception;

    public abstract void asyncSend(T msg, String topic, String flag) throws Exception;

    public abstract void syncSend(T msg) throws Exception;

    public abstract void syncSend(T msg, String topic, String flag) throws Exception;

    public abstract void syncSend(T msg, int delay) throws Exception;

    public abstract void syncSend(T msg, int delay, String topic, String flag) throws Exception;

    public abstract void doAfterSyncSend(T msg, SendResult sendResult) throws Exception;
}
