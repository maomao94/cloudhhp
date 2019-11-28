package com.hehanpeng.framework.cloudhhp.module.core.frame.workflow;

import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务处理代理类
 *
 * @author hehanpeng
 */
public class BusinessProcessorProxy {

    /**
     * 业务处理器
     */
    private BusinessProcessor businessProcessor;

    /**
     * 下一个工作任务集合
     */
    private Map<String, String> nextWorks = new HashMap<>();

    /**
     * 超时时间
     */
    private Long timeout;

    /**
     * 执行实际业务逻辑
     *
     * @param context 交易上下文
     */
    public void process(CoreFlowContext context) throws Exception {
        if (timeout != null) {
            //todo 注册部分超时
            //下游系统未在时间内给响应，存储转发回调core重启工作流，走超时 processResult=TIMEOUT 的处理器
        }

        if (businessProcessor != null) {
            businessProcessor.process(context);
        }
    }

    public void setBusinessProcessor(BusinessProcessor businessProcessor) {
        this.businessProcessor = businessProcessor;
    }

    public Map<String, String> getNextWorks() {
        return nextWorks;
    }

    public void setNextWorks(Map<String, String> nextWorks) {
        this.nextWorks = nextWorks;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}

