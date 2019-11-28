package com.hehanpeng.framework.cloudhhp.module.forward.schedule;

import com.hehanpeng.framework.cloudhhp.common.constants.CommonConstants;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdRegisterMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.TtoInfMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdRegister;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.TtoInf;
import com.hehanpeng.framework.cloudhhp.module.forward.service.AsyncDealFwdRegisterStoreForwardService;
import com.hehanpeng.framework.cloudhhp.module.forward.service.AsyncDealTtoInfoTimeOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hehanpeng
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    AsyncDealTtoInfoTimeOutService asyncDealTtoInfoTimeOutService;

    @Autowired
    AsyncDealFwdRegisterStoreForwardService asyncDealFwdRegisterStoreForwardService;

    @Autowired(required = false)
    private TtoInfMapper ttoInfMapper;

    @Autowired(required = false)
    private FwdRegisterMapper fwdRegisterMapper;

    @Scheduled(fixedRate = 1000)
    public void dealTtoInfoTimeOut() {
        Long start = System.currentTimeMillis();
        try {
            List<String> ttoTypeList = new ArrayList<>();
            ttoTypeList.add(ForwardParamConstant.TTOINF_TYPE_GLOBAL);
            ttoTypeList.add(ForwardParamConstant.TTOINF_TYPE_LOCAL);

            Example example = new Example(TtoInf.class);
            Example.Criteria criteria = example.createCriteria()
                    .andEqualTo("ttoStatus", ForwardParamConstant.TTOINF_STATUS_UNDEAL)
                    .andLessThanOrEqualTo("excuteTime", new Date())
                    .andIn("ttoType", ttoTypeList);
            List<TtoInf> ttoInfs = ttoInfMapper.selectByExample(example);
            if (!ttoInfs.isEmpty()) {
                for (TtoInf u : ttoInfs) {
                    //异步调用，有重复转发的风险
                    asyncDealTtoInfoTimeOutService.execute(u);
                }
                Long end = System.currentTimeMillis();
                log.info("startTime={},endTime={},duration={},size={},dealTtoInfoTimeOut end", start, end, end - start,
                        ttoInfs.size());
            }
        } catch (Throwable e) {
            log.error("dealTtoInfoTimeOut-Throwable", e);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void dealFwdRegister() {
        Long start = System.currentTimeMillis();
        try {
            List<String> statusList = new ArrayList<String>();
            statusList.add(ForwardParamConstant.FWDREG_STATUS_WAIT);
            statusList.add(ForwardParamConstant.FWDREG_STATUS_FORWARD);
            List<String> typeList = new ArrayList<>();
            typeList.add(CommonConstants.FWD_TYPE.FWDTYPE_NORMAL);
            typeList.add(CommonConstants.FWD_TYPE.FWDTYPE_POW);
            Example example = new Example(FwdRegister.class);
            //设置查询条件：全局/局部、重试次数小于重试次数上限、激活时间小于等于当前时间，状态为等待、转发中
            Example.Criteria criteria = example.createCriteria()
                    .andIn("fwdType", typeList)
                    .andCondition("RETRY_COUNT < RETRY_LIMIT")
                    .andLessThanOrEqualTo("nextActiveTime", new Date())
                    .andIn("fwdStatus", statusList);
            List<FwdRegister> fwdRegisters = fwdRegisterMapper.selectByExample(example);

            if (!fwdRegisters.isEmpty()) {
                for (FwdRegister u : fwdRegisters) {
                    //异步调用，有重复转发的风险
                    asyncDealFwdRegisterStoreForwardService.execute(u);
                }
                Long end = System.currentTimeMillis();
                log.info("startTime={},endTime={},duration={},size={},dealTtoInfoTimeOut end", start, end, end - start,
                        fwdRegisters.size());
            }
        } catch (Throwable e) {
            log.error("dealFwdRegister-Throwable", e);
        }
    }
}
