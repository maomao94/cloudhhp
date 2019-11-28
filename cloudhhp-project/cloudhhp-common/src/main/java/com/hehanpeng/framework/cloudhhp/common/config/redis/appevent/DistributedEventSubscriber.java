package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

public interface DistributedEventSubscriber {

    /**
     * 通知分布式应用事件
     * @param appEvent
     */
    void notifyEvent(DistributedEvent appEvent);
}
