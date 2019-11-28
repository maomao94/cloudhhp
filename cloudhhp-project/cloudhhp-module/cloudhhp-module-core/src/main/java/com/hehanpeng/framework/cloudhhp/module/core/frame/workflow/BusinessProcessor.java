package com.hehanpeng.framework.cloudhhp.module.core.frame.workflow;

import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;

/**
 * 业务处理接口
 */
public interface BusinessProcessor {

    /**
     * 处理业务
     *
     * @param context 交易上下文
     * @throws Exception
     */
    void process(CoreFlowContext context) throws Exception;
}
