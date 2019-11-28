package com.hehanpeng.framework.cloudhhp.module.core.frame.chain;

import java.util.List;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description: 组合处理器
 * @date 2017-07-05
 */
public class UnitChain<T> implements IChainProcessor<T> {

    /**
     * 业务处理器列表
     */
    private List<IChainProcessor<T>> processors;

    /**
     * 外层继续,用在子流程跳过,外流程继续
     */
    private boolean outerContinue = false;

    @Override
    public boolean process(T ctx) throws Exception {
        boolean isContinue = true;
        if (processors != null) {
            for (IChainProcessor<T> p : processors) {
                isContinue = p.process(ctx);
                if (!isContinue) {
                    break;
                }
            }
        }

        if (this.outerContinue) {
            return true;
        }

        return isContinue;
    }

    public void setProcessors(List<IChainProcessor<T>> processors) {
        this.processors = processors;
    }

    public void setOuterContinue(boolean outerContinue) {
        this.outerContinue = outerContinue;
    }
}
