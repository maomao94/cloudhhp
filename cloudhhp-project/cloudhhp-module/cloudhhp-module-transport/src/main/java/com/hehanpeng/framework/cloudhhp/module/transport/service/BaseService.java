package com.hehanpeng.framework.cloudhhp.module.transport.service;

import com.hehanpeng.framework.cloudhhp.module.transport.sequence.SequenceService;
import com.hehanpeng.framework.cloudhhp.module.transport.service.redis.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class BaseService {
    @Autowired
    public RedisLockService redisLockService;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public SequenceService sequenceService;
}
