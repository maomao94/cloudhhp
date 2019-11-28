package com.hehanpeng.framework.cloudhhp.module.core.frame.chain;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description: 单元处理器基类
 * @date 2017-07-05
 */
public abstract class AbstractProcessorUnit<T> implements IChainProcessor<T> {

    @Override
    public boolean process(T ctx) throws Exception {

        try {
            doCheck(ctx);
            return doProcess(ctx);
        } catch (Exception e) {
            dealException(ctx, e);
            throw e;
        }
    }

    /**
     * 子类覆写此方法，执行具体的前置检查
     *
     * @param ctx 上下文
     * @throws BizException
     */
    protected void doCheck(T ctx) throws BizException {
        //ignore
    }

    /**
     * 子类覆写此方法，执行具体的异常处理
     *
     * @param ctx 上下文
     * @param e   业务异常
     * @throws Exception
     */
    protected void dealException(T ctx, Exception e) {

    }

    /**
     * 子类覆写此方法，执行具体的业务逻辑
     *
     * @param ctx 上下文
     * @throws BizException
     */
    abstract protected boolean doProcess(T ctx) throws Exception;
}
