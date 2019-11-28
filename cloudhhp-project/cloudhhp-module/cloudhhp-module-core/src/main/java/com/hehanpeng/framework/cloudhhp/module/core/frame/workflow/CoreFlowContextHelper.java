package com.hehanpeng.framework.cloudhhp.module.core.frame.workflow;

import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;
import org.springframework.stereotype.Service;

/**
 * 数据上下文帮助类获取
 *
 * @author hehanpeng
 */
@Service
public class CoreFlowContextHelper {

    /**
     * threadlocal
     */
    private static ThreadLocal<CoreFlowContext> coreFlowContextThreadLocal = new ThreadLocal<CoreFlowContext>();

    /**
     * 添加上下文
     *
     * @param context
     */
    public void addFlowContext(CoreFlowContext context) {
        coreFlowContextThreadLocal.set(context);
    }

    /**
     * 获取当前交易上下文
     *
     * @return 当前的交易上下文
     */
    public CoreFlowContext getFlowContext() {
        return coreFlowContextThreadLocal.get();
    }

    public CoreFlowContext createFlowContext() {
        CoreFlowContext context = new CoreFlowContext();
        coreFlowContextThreadLocal.set(context);
        return context;
    }

    public void removeThreadLocal() {
        coreFlowContextThreadLocal.remove();
    }
}
