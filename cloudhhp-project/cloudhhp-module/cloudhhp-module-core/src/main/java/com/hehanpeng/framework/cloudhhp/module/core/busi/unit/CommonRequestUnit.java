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

import java.util.Date;

/**
 * 链路节点：请求类型，构建CONTEXT,进入不同业务的工作流
 *
 * @author hehanpeng
 */
@Slf4j
@Component("commonRequestUnit")
public class CommonRequestUnit extends AbstractProcessorUnit<MqMessage> {

    @Autowired
    CoreFlowContextManager coreFlowContextManager;
    @Autowired
    private CoreFlowContextHelper coreFlowContextHelper;
    @Autowired
    private WorkFlowProcessor workFlowProcessor;

    @Override
    protected boolean doProcess(MqMessage msg) throws Exception {
        buildCoreFlowContext(msg);

        //开启工作流
        workFlowProcessor.process();

        return true;
    }

    private void buildCoreFlowContext(MqMessage msg) throws Exception {
        //构建CoreFlowContext
        CoreFlowContext context = coreFlowContextHelper.createFlowContext();
        //start 标识
        context.setCurrentWork(WorkflowConstant.WORK_START);
        context.setFlowStatus(WorkflowConstant.CONTEXT_STATUS_PENDDING);
        context.setProcessResult(WorkflowConstant.PROCESS_CONTINUE);
        context.setWorkflowName(msg.getBizType());
        context.setReqTime(new Date());
        coreFlowContextManager.insertContext(context);
    }
}
