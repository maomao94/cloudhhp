package com.hehanpeng.framework.cloudhhp.module.transport.service;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.MqBusiService;

public interface TransportService extends MqBusiService<MqMessage> {

    /**
     * 测试工作流
     */
    void testFlow(MqMessage msg) throws Exception;
}
