package com.hehanpeng.framework.cloudhhp.module.forward.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.DictFwdRegisterData;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdDetailMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdRegisterMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdDetail;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdRegister;
import com.hehanpeng.framework.cloudhhp.module.forward.service.AsyncDealFwdRegisterStoreForwardService;
import com.hehanpeng.framework.cloudhhp.module.forward.util.DateUtil;
import com.hehanpeng.framework.cloudhhp.module.forward.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncDealFwdRegisterStoreForwardServiceImpl implements AsyncDealFwdRegisterStoreForwardService {

    private int lockWaitTime = 10000;

    private int lockLeaseTime = 10000;

    @Autowired(required = false)
    private FwdRegisterMapper fwdRegisterMapper;

    @Autowired(required = false)
    private FwdDetailMapper fwdDetailMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Async("asyncServiceExecutor")
    @Transactional
    @Override
    public void execute(FwdRegister input) {
        Long start = System.currentTimeMillis();
        log.info(">>>AsyncDealFwdRegisterStoreForwardService param:{}>>>", input);
        String key = "StoreForward_ID_" + input.getId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("thread", Thread.currentThread().getName());
        jsonObject.put("startTime", start);

        String invokeStatus = "";

        jsonObject.put(DictFwdRegisterData.ID, input.getId());
        jsonObject.put(DictFwdRegisterData.REFERENCE, input.getReference());

        // 上锁
        RLock rlock = redissonClient.getLock(key);
        try {
            boolean flag = rlock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.MILLISECONDS);
            if (!flag) {
                log.info("存储转发处理,转发注册记录[{}]获取锁失败", key);
                return;
            } else {
                input.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_FORWARD);
                input.setRetryCount(input.getRetryCount() + 1);

                // 执行前需要插入存储转发动作明细表
                FwdDetail fwdDetail = new FwdDetail();
                fwdDetail.setRegid(input.getId());

                fwdDetailMapper.insertSelective(fwdDetail);

                Map<String, Object> reqMap = new HashMap<>();
                reqMap.put(DictFwdRegisterData.ID, input.getId());
                reqMap.put(DictFwdRegisterData.REFERENCE, input.getReference());
                reqMap.put(DictFwdRegisterData.CALLCLASS, input.getCallClass());
                reqMap.put(DictFwdRegisterData.CALLMETHOD, input.getCallMethod());
                reqMap.put(DictFwdRegisterData.RETRYCOUNT, input.getRetryCount());

                // 更新转发登记表中的重试次数和和下次激活时间
                if (input.getRetryCount().intValue() == input.getRetryLimit().intValue()) {
                    input.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_UPPERLIMIT);
                } else {
                    input.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_FORWARD);
                }
                Date nextActiveTime = DateUtil.calcNextActiveTime(input.getRetryLimit(), input.getRetryCount(), input.getBaseInterval(),
                        input.getIntervalDelta(), input.getFwdType());
                input.setNextActiveTime(nextActiveTime);
                Example example = new Example(FwdRegister.class);
                Example.Criteria criteria = example.createCriteria()
                        .andEqualTo("id", input.getId())
                        .andNotEqualTo("fwdStatus", ForwardParamConstant.FWDREG_STATUS_CANCEL);
                fwdRegisterMapper.updateByExampleSelective(input, example);
                log.info("id={},reference={},status={},update fwd register", input.getId(), input.getReference(), input.getFwdStatus());
                Date forwardTime = new Date();
                //dubbo 调用
                Class<?> cl = Class.forName(input.getCallClass());
                SpringUtils.springInvokeMethod(cl, input.getCallMethod(), new Object[]{reqMap});
                fwdDetail.setForwardTime(forwardTime);
                fwdDetail.setForwardFlag(invokeStatus);
                fwdDetailMapper.updateByPrimaryKeySelective(fwdDetail);
            }
        } catch (Exception e) {
            log.error("param:{} AsyncDealFwdRegisterStoreForwardService error:{} ", input, e);
        } finally {
            if (rlock.isHeldByCurrentThread()) {
                try {
                    rlock.unlock();
                } catch (Exception e) {
                    log.error("存储转发处理,记录[{}]解锁失败", key, e);
                }
            }
        }
    }
}