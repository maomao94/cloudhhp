package com.hehanpeng.framework.cloudhhp.module.transport.mq.consumer;

/**
 * @author hehanpeng
 * 2019/4/29 20:43
 */
public abstract class AbstractMqConsumer<T> {
    public abstract void handler(T msg) throws Exception;
}
