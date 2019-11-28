package com.hehanpeng.framework.cloudhhp.module.forward.service.impl;

import com.hehanpeng.framework.cloudhhp.module.forward.constant.DictTtoInfoData;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.TtoInfMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.TtoInf;
import com.hehanpeng.framework.cloudhhp.module.forward.service.AsyncDealTtoInfoTimeOutService;
import com.hehanpeng.framework.cloudhhp.module.forward.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hehanpeng
 * 2018/9/17 15:46
 */
@Slf4j
@Service
public class AsyncDealTtoInfoTimeOutServiceImpl implements AsyncDealTtoInfoTimeOutService {

    private int lockWaitTime = 10000;

    private int lockLeaseTime = 10000;

    @Autowired(required = false)
    private TtoInfMapper ttoInfMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Async("asyncServiceExecutor")
    @Transactional
    @Override
    public void execute(TtoInf input) {
        Long start = System.currentTimeMillis();
        log.info(">>>AsyncDealTtoInfoTimeOutService ttoid-{} start param: {}>>>", input);
        String key = "ttoid_" + input.getTtoid();

        // 上锁
        RLock rlock = redissonClient.getLock(key);
        try {
            boolean flag = rlock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.MILLISECONDS);
            if (!flag) {
                log.info("超时转发处理,转发注册记录[{}]获取锁失败", key);
                return;
            } else {
                TtoInf record = new TtoInf();
                record.setTtoid(input.getTtoid());
                record.setTtoStatus(ForwardParamConstant.TTOINF_STATUS_DEALED);
                Map<String, Object> reqMap = new HashMap<>();
                reqMap.put(DictTtoInfoData.TTOID, input.getTtoid());
                reqMap.put(DictTtoInfoData.REFERENCE, input.getReference());
                reqMap.put(DictTtoInfoData.BIZTYPE, input.getBizType());
                reqMap.put(DictTtoInfoData.TTOTYPE, input.getTtoType());
                reqMap.put(DictTtoInfoData.EXT1, input.getExt1());
                reqMap.put(DictTtoInfoData.EXT2, input.getExt2());
                reqMap.put(DictTtoInfoData.EXT3, input.getExt3());
                reqMap.put(DictTtoInfoData.EXT4, input.getExt4());
                reqMap.put(DictTtoInfoData.EXT5, input.getExt5());
                //dubbo 调用
                Class<?> cl = Class.forName(input.getCallClass());
                SpringUtils.springInvokeMethod(cl, input.getCallMethod(), new Object[]{reqMap});
                ttoInfMapper.updateByPrimaryKeySelective(record);
            }
        } catch (Exception e) {
            log.error("param:{},AsyncDealTtoInfoTimeOutService error: {}", input, e);
        } finally {
            if (rlock.isHeldByCurrentThread()) {
                try {
                    rlock.unlock();
                } catch (Exception e) {
                    log.error("超时转发处理,记录[{}]解锁失败", key, e);
                }
            }
        }
    }
}