package com.hehanpeng.framework.cloudhhp.module.core.service;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.mq.MqBusiService;

public interface CoreService extends MqBusiService<MqMessage> {

    /**
     * 测试
     */
    void test(MqMessage msg) throws Exception;

    /**
     * 测试工作流
     */
    void testFlow(MqMessage msg) throws Exception;
    /**
     * 测试工作流-响应部分
     */
    void testFlowResp(MqMessage msg) throws Exception;
}
