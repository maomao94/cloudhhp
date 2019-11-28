package com.hehanpeng.framework.cloudhhp.module.transport.mq;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.transport.mq.consumer.MqConsumerFunc;
import com.hehanpeng.framework.cloudhhp.module.transport.service.TransportService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hehanpeng
 * 2019/5/22 10:39
 */
@Component
public class MqTransportFuncFactory {
    private static Map<String, MqConsumerFunc<TransportService, MqMessage>> transportServiceMethod = new HashMap<>();

    static {
        transportServiceMethod.put("testFlow", TransportService::testFlow);
    }

    public static MqConsumerFunc getMethod(String bizType) {
        return Optional.ofNullable(transportServiceMethod.get(bizType)).orElseThrow(
                () -> new BizException("this bizType named " + bizType + " in MqTransportFuncFactory is " + "unsupported"));
    }
}
