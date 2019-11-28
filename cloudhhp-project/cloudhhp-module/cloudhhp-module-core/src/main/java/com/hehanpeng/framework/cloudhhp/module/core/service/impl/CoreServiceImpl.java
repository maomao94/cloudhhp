package com.hehanpeng.framework.cloudhhp.module.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.annotation.MqBusiHandler;
import com.hehanpeng.framework.cloudhhp.module.core.event.DefaultEventManager;
import com.hehanpeng.framework.cloudhhp.module.core.event.handler.TestEvent;
import com.hehanpeng.framework.cloudhhp.module.core.frame.chain.ChainProcessorFactory;
import com.hehanpeng.framework.cloudhhp.module.core.frame.chain.IChainProcessor;
import com.hehanpeng.framework.cloudhhp.module.core.mq.MqBusiServiceCallback;
import com.hehanpeng.framework.cloudhhp.module.core.mq.MqCoreFuncFactory;
import com.hehanpeng.framework.cloudhhp.module.core.service.BaseService;
import com.hehanpeng.framework.cloudhhp.module.core.service.CoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
@MqBusiHandler(transNetId = "1")
public class CoreServiceImpl extends BaseService implements CoreService {

    @Autowired
    public DefaultEventManager defaultEventManager;
    @Autowired
    private ChainProcessorFactory<MqMessage> chainProcessorFactory;

    @Override
    public void test(MqMessage msg) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("logId={},{} start", msg.getLogId(), methodName);
        //todo
        msg.setCoreId(sequenceService.nextId("core"));
        msg.setDesc("core 响应");
        String key = "core" + msg.getLogId();
        redisTemplate.opsForList().leftPush(key, JSON.toJSONString(msg));
        redisTemplate.expire(key, 10000, TimeUnit.MILLISECONDS);
        //执行异步TestEvent
        defaultEventManager.doService(new TestEvent());
        log.info("logId={},{} end", msg.getLogId(), methodName);
    }

    @Override
    public void testFlow(MqMessage msg) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("logId={},{} start", msg.getLogId(), methodName);
        msg.setCoreId(sequenceService.nextId("core"));
        msg.setDesc("core 链式调用响应");
        String key = "core" + msg.getLogId();
        //开启链式调用
        IChainProcessor<MqMessage> requestProcessor = chainProcessorFactory.getRequestProcessor(msg.getBizType());
        requestProcessor.process(msg);
        redisTemplate.opsForList().leftPush(key, JSON.toJSONString(msg));
        redisTemplate.expire(key, 10000, TimeUnit.MILLISECONDS);
        log.info("logId={},{} end", msg.getLogId(), methodName);
    }

    @Override
    public void testFlowResp(MqMessage msg) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("logId={},{} start", msg.getLogId(), methodName);
        //开启链式调用
        IChainProcessor<MqMessage> requestProcessor = chainProcessorFactory.getResponseProcessor(msg.getBizType());
        requestProcessor.process(msg);
        log.info("logId={},{} end", msg.getLogId(), methodName);
    }

    @Override
    public void asyncExecute(MqMessage msg, MqBusiServiceCallback callback) {

    }

    @Override
    public void asyncExecuteFunc(MqMessage msg, MqBusiServiceCallback callback) {
        try {
            redisLockService.lockLogId(msg);
            MqCoreFuncFactory.getMethod(msg.getBizType()).execute(this, msg);
            callback.onSuccess(msg);
        } catch (Throwable e) {
            callback.onException(e);
        } finally {
            redisLockService.unlockLogId(msg);
        }
    }
}
