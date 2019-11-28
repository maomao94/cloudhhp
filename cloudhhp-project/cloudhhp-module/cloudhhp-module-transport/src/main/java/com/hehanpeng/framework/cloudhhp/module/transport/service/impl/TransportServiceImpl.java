package com.hehanpeng.framework.cloudhhp.module.transport.service.impl;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.transport.annotation.MqBusiHandler;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.MqBusiServiceCallback;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.MqTransportFuncFactory;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.producer.impl.RocketMq2CoreProducer;
import com.hehanpeng.framework.cloudhhp.module.transport.service.BaseService;
import com.hehanpeng.framework.cloudhhp.module.transport.service.TransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@MqBusiHandler(transNetId = "1")
public class TransportServiceImpl extends BaseService implements TransportService {
    @Autowired
    RocketMq2CoreProducer rocketMq2CoreProducer;

    @Override
    public void testFlow(MqMessage msg) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("logId={},{} start", msg.getLogId(), methodName);
        msg.setDesc("transport 响应");
        rocketMq2CoreProducer.asyncSend(msg);
        log.info("logId={},{} end", msg.getLogId(), methodName);
    }

    @Override
    public void asyncExecute(MqMessage msg, MqBusiServiceCallback callback) {

    }

    @Override
    public void asyncExecuteFunc(MqMessage msg, MqBusiServiceCallback callback) {
        try {
            redisLockService.lockLogId(msg);
            MqTransportFuncFactory.getMethod(msg.getBizType()).execute(this, msg);
            callback.onSuccess(msg);
        } catch (Throwable e) {
            callback.onException(e);
        } finally {
            redisLockService.unlockLogId(msg);
        }
    }
}
