package com.hehanpeng.framework.cloudhhp.module.core.service.redis;

import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;

public interface RedisLockService {

    void lockLogId(MqMessage msg);

    void unlockLogId(MqMessage msg);
}
