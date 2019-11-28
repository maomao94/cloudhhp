package com.hehanpeng.framework.cloudhhp.module.core.frame.chain;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.module.core.constant.CoreErrEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description: 业务处理器工厂
 * @date 2017-07-05
 */
public class ChainProcessorFactory<T> {

    /**
     * 请求业务处理器映射
     */
    private Map<String, IChainProcessor<T>> requestProcessorMap = new HashMap<String, IChainProcessor<T>>();

    /**
     * 响应业务处理器映射
     */
    private Map<String, IChainProcessor<T>> responseProcessorMap = new HashMap<String, IChainProcessor<T>>();

    /**
     * 超时业务处理器映射
     */
    private Map<String, IChainProcessor<T>> timeoutProcessorMap = new HashMap<String, IChainProcessor<T>>();

    /**
     * 通知业务处理器映射
     */
    private Map<String, IChainProcessor<T>> notifyProcessorMap = new HashMap<String, IChainProcessor<T>>();

    /**
     * 转发业务处理器映射
     */
    private Map<String, IChainProcessor<T>> forwardProcessorMap = new HashMap<String, IChainProcessor<T>>();

    public IChainProcessor<T> getRequestProcessor(String key) throws BizException {
        return requestProcessorMap.get(key);
    }

    public IChainProcessor<T> getResponseProcessor(String key) throws BizException {
        if (key == null || key.trim().length() == 0) {
            throw new BizException(CoreErrEnum.ERR_0001.getCode(), "Choose ResponseProcessor input error.");
        }
        return responseProcessorMap.get(key);
    }

    public IChainProcessor<T> getTimeoutProcessor(String key) throws BizException {
        if (key == null || key.trim().length() == 0) {
            throw new BizException(CoreErrEnum.ERR_0001.getCode(), "Choose TimeoutProcessor input error.");
        }
        return timeoutProcessorMap.get(key);
    }

    public IChainProcessor<T> getNotifyProcessor(String key) throws BizException {
        if (key == null || key.trim().length() == 0) {
            throw new BizException(CoreErrEnum.ERR_0001.getCode(), "Choose NotifyProcessor input error.");
        }
        return notifyProcessorMap.get(key);
    }

    public IChainProcessor<T> getForwardProcessor(String key) throws BizException {
        if (key == null || key.trim().length() == 0) {
            throw new BizException(CoreErrEnum.ERR_0001.getCode(), "Choose ForwardProcessor input error.");
        }
        return forwardProcessorMap.get(key);
    }

    public void setRequestProcessorMap(Map<String, IChainProcessor<T>> requestProcessorMap) {
        this.requestProcessorMap = requestProcessorMap;
    }

    public void setResponseProcessorMap(Map<String, IChainProcessor<T>> responseProcessorMap) {
        this.responseProcessorMap = responseProcessorMap;
    }

    public void setTimeoutProcessorMap(Map<String, IChainProcessor<T>> timeoutProcessorMap) {
        this.timeoutProcessorMap = timeoutProcessorMap;
    }

    public void setNotifyProcessorMap(Map<String, IChainProcessor<T>> notifyProcessorMap) {
        this.notifyProcessorMap = notifyProcessorMap;
    }

    public void setForwardProcessorMap(Map<String, IChainProcessor<T>> forwardProcessorMap) {
        this.forwardProcessorMap = forwardProcessorMap;
    }
}
