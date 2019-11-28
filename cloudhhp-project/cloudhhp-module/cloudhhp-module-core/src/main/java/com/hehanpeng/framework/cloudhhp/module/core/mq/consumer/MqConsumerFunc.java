package com.hehanpeng.framework.cloudhhp.module.core.mq.consumer;

/**
 * @author hehanpeng
 * 2019/5/22 10:48
 */
@FunctionalInterface
public interface MqConsumerFunc<S,T> {
    void execute(S service, T msg) throws Exception;
}