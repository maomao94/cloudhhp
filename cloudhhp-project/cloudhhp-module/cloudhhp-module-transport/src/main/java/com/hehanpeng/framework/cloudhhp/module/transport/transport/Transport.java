package com.hehanpeng.framework.cloudhhp.module.transport.transport;

import com.hehanpeng.framework.cloudhhp.common.exception.CommunicationException;

/**
 * created with Intellij IDEA 2017.2
 *
 * @author: hehanpeng
 * Email: 287737281@qq.com
 * Date: 2019/6/3
 * Time: 9:41
 * 下游通道通讯接口
 */
public interface Transport<T, M, S> {
    /**
     * 无出参submit
     *
     * @param t   请求bean
     * @param msg mq消息
     * @throws CommunicationException
     */
    void submit(T t, M msg) throws CommunicationException;

    /**
     * 出参submit
     *
     * @param t           请求bean
     * @param msg         mq消息
     * @param outputClazz 响应class
     * @param <O>
     * @return 响应bean
     * @throws CommunicationException
     */
    <O extends S> O submit(T t, M msg, Class<O> outputClazz) throws CommunicationException;

    /**
     * 出参submit,无mq消息
     *
     * @param t           请求bean
     * @param outputClazz 响应class
     * @param <O>
     * @return 响应bean
     * @throws CommunicationException
     */
    <O extends S> O submit(T t, Class<O> outputClazz) throws CommunicationException;
}
