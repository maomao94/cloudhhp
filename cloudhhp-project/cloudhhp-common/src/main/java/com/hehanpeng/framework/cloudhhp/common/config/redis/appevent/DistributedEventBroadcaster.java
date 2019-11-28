package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

public interface DistributedEventBroadcaster {
    /**
     * 发布分布式应用事件
     */
    void publishEvent(DistributedEvent distributedEvent);
}
