package com.hehanpeng.framework.cloudhhp.module.transport.mq;
/**
 * @author hehanpeng
 * 2019/4/30 15:49
 * 消息业务处理服务
 */
public interface MqBusiService<T> {
    /**
     * 异步调用
     * @param msg 消息体
     * @param callback 回调
     */
    void asyncExecute(T msg, MqBusiServiceCallback callback);

    /**
     * 异步函数式调用
     * @param msg 消息体
     * @param callback 回调
     */
    void asyncExecuteFunc(T msg, MqBusiServiceCallback callback);
}