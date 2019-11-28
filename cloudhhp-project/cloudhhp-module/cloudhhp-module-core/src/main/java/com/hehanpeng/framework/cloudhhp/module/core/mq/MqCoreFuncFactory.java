package com.hehanpeng.framework.cloudhhp.module.core.mq;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.mq.consumer.MqConsumerFunc;
import com.hehanpeng.framework.cloudhhp.module.core.service.CoreService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hehanpeng
 * 2019/5/22 10:39
 */
@Component
public class MqCoreFuncFactory {
    private static Map<String, MqConsumerFunc<CoreService, MqMessage>> coreServiceMethod = new HashMap<>();

    static {
        coreServiceMethod.put("test", CoreService::test);
        coreServiceMethod.put("testFlow", CoreService::testFlow);
        coreServiceMethod.put("testFlowResp", CoreService::testFlowResp);
    }

    public static MqConsumerFunc getMethod(String bizType) {
        return Optional.ofNullable(coreServiceMethod.get(bizType)).orElseThrow(
                () -> new BizException("this bizType named " + bizType + " in MqCoreFuncFactory is " + "unsupported"));
    }
}
