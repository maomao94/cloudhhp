package com.hehanpeng.framework.cloudhhp.module.transport.mq;

/**
 * @author hehanpeng
 * 2019/5/7 19:15
 */
public interface MqBusiServiceCallback<T> {
    void onSuccess(final T msg);

    void onException(final Throwable e);
}
