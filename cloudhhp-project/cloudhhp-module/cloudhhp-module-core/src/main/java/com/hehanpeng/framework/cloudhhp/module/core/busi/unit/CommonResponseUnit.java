package com.hehanpeng.framework.cloudhhp.module.core.busi.unit;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.core.constant.WorkflowConstant;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;
import com.hehanpeng.framework.cloudhhp.module.core.frame.chain.AbstractProcessorUnit;
import com.hehanpeng.framework.cloudhhp.module.core.frame.workflow.CoreFlowContextHelper;
import com.hehanpeng.framework.cloudhhp.module.core.frame.workflow.WorkFlowProcessor;
import com.hehanpeng.framework.cloudhhp.module.core.manage.CoreFlowContextManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 链路节点：响应
 *
 * @author hehanpeng
 */
@Slf4j
@Component("commonResponseUnit")
public class CommonResponseUnit extends AbstractProcessorUnit<MqMessage> {
    @Autowired
    CoreFlowContextManager coreFlowContextManager;
    @Autowired
    private CoreFlowContextHelper coreFlowContextHelper;
    @Autowired
    private WorkFlowProcessor workFlowProcessor;

    @Override
    protected boolean doProcess(MqMessage msg) throws Exception {
        buildTxnContext(msg);
        workFlowProcessor.process();
        return true;
    }

    private void buildTxnContext(MqMessage msg) throws Exception {
        //构建CoreFlowContext
        CoreFlowContext context = coreFlowContextHelper.getFlowContext();
        context.setFlowStatus(WorkflowConstant.CONTEXT_STATUS_PENDDING);
        context.setProcessResult(WorkflowConstant.PROCESS_CONTINUE);
        coreFlowContextManager.updateContext(context);
    }
}
