package com.hehanpeng.framework.cloudhhp.module.core.busi.unit;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.frame.chain.AbstractProcessorUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 链路节点：过滤器
 */
@Component("filterUnit")
@Slf4j
public class FilterUnit extends AbstractProcessorUnit<MqMessage> {

    @Override
    protected boolean doProcess(MqMessage msg) throws BizException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("FilterUnit {},{} start", msg, methodName);
        log.info("FilterUnit {},{} end", msg, methodName);
        return true;
    }
}
