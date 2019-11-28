package com.hehanpeng.framework.cloudhhp.module.core.frame.workflow;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.module.core.constant.CoreErrEnum;
import com.hehanpeng.framework.cloudhhp.module.core.constant.WorkflowConstant;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流引擎
 *
 * @author hehanpeng
 */
@Slf4j
public class WorkFlowEngine {
    /**
     * 交易上下文帮助类
     */
    @Autowired
    CoreFlowContextHelper coreFlowContextHelper;

    /**
     * 工作流集合，渠道类型-->Map<处理器名称-->处理器>
     */
    private Map<String, Map<String, BusinessProcessorProxy>> workFlows = new HashMap<String, Map<String, BusinessProcessorProxy>>();

    /**
     * 执行业务工作流
     */
    void processWork() {
        CoreFlowContext context = coreFlowContextHelper.getFlowContext();
        try {
            if (WorkflowConstant.PROCESS_WAIT.equals(context.getProcessResult())) {
                // 异步
                return;
            }

            String nextWorkValue = null;
            BusinessProcessorProxy nextProcessor = null;

            // 获取渠道类型的工作流
            Map<String, BusinessProcessorProxy> workFlow = getWorkFlow(context);
            BusinessProcessorProxy currentProcessor = workFlow.get(context.getCurrentWork());
            if (context.getProcessResult() != null) {
                nextWorkValue = currentProcessor.getNextWorks().get(context.getProcessResult());
                nextProcessor = workFlow.get(nextWorkValue);
            }

            // 结束
            if (nextProcessor == null) {
                coreFlowContextHelper.removeThreadLocal();
                return;
            }

            context.setProcessResult(null);
            context.setCurrentWork(nextWorkValue);
            nextProcessor.process(context);
        } catch (BizException be) {
            // 系统异常
            return;
        } catch (Exception e) {
            // 系统异常
            return;
        }

        // 处理下一流程
        processWork();
    }

    /**
     * 获取渠道类型的工作流
     *
     * @param context 交易上下文
     * @return 交易渠道的工作流
     * @throws BizException
     */
    private Map<String, BusinessProcessorProxy> getWorkFlow(CoreFlowContext context) throws BizException {
        Map<String, BusinessProcessorProxy> workFlow = workFlows.get(context.getWorkflowName());
        if (workFlow == null) {
            log.error("workFlowEngine,can not found workflow,ContextId={},workflowName={}", context.getIdFlowContext(), context.getWorkflowName());
            throw new BizException(CoreErrEnum.ERR_0001.getCode(), CoreErrEnum.ERR_0001.getMessage());
        }

        return workFlow;
    }

    public void setWorkFlows(Map<String, Map<String, BusinessProcessorProxy>> workFlows) {
        this.workFlows = workFlows;
    }
}
