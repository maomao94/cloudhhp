package com.hehanpeng.framework.cloudhhp.module.core.frame.workflow;

import com.hehanpeng.framework.cloudhhp.module.core.constant.WorkflowConstant;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流处理器
 *
 * @author hehanpeng
 */
@Slf4j
@Component
public class WorkFlowProcessor implements IWorkFlowProcessor {
    @Autowired
    private CoreFlowContextHelper coreFlowContextHelper;

    private WorkFlowEngine workFlowEngine;

    private Long defaultGlobalTimeout;

    private Map<String, Long> globalTimeout = new HashMap<>();

    @Override
    public void process() {
        CoreFlowContext context = coreFlowContextHelper.getFlowContext();
        log.info("WorkFlowProcessor start");
        //若为第一次进入工作流则注册全局超时，若为重启工作流（比如响应回来后重启原工作流）不注册
        if (WorkflowConstant.WORK_START.equals(context.getCurrentWork())) {
            //todo 注册全局超时处理
        }

        workFlowEngine.processWork();
        log.info("WorkFlowProcessor end");
    }

    public void setWorkFlowEngine(WorkFlowEngine workFlowEngine) {
        this.workFlowEngine = workFlowEngine;
    }

    public void setDefaultGlobalTimeout(Long defaultGlobalTimeout) {
        this.defaultGlobalTimeout = defaultGlobalTimeout;
    }

    public void setGlobalTimeout(Map<String, Long> globalTimeout) {
        this.globalTimeout = globalTimeout;
    }
}
