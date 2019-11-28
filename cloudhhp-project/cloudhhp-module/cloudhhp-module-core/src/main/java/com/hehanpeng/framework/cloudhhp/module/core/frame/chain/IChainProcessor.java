package com.hehanpeng.framework.cloudhhp.module.core.frame.chain;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description: 处理器
 * @date 2017-07-05
 */
public interface IChainProcessor<T> {

    /**
     * 业务处理
     *
     * @param ctx
     * @return 是否继续后续流程
     * @throws Exception
     */
    boolean process(T ctx) throws Exception;
}