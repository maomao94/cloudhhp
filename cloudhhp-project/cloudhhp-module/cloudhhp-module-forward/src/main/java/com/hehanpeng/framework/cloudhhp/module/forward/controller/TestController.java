package com.hehanpeng.framework.cloudhhp.module.forward.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hehanpeng.framework.cloudhhp.common.constants.CommonConstants;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdDetailMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdRegisterMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.TtoInfMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdDetail;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdRegister;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.TtoInf;
import com.hehanpeng.framework.cloudhhp.module.forward.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired(required = false)
    private FwdRegisterMapper fwdRegisterMapper;

    @Autowired(required = false)
    private TtoInfMapper ttoInfMapper;

    @Autowired(required = false)
    private FwdDetailMapper fwdDetailMapper;

    @RequestMapping("/addForward")
    public void addForward(int num) {
        Long orderId = System.currentTimeMillis() + RandomUtils.nextInt(0, 10000);
        for (int i = 0; i < num; i++) {
            FwdRegister fwdRegister = new FwdRegister();
            fwdRegister.setBaseInterval(5000L);
            fwdRegister.setBizType("00200");
            fwdRegister.setCallClass("com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.HelloDubboService");
            fwdRegister.setCallMethod("testFwd");
            fwdRegister.setCreateTime(new Date());
            fwdRegister.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_WAIT);
            fwdRegister.setFwdType("0");
            fwdRegister.setReference(orderId);
            fwdRegister.setLastUpdateTime(new Date());
            fwdRegister.setNextActiveTime(DateUtil.calcNextActiveTime(1, 0,
                    5000, 1000, CommonConstants.FWD_TYPE.FWDTYPE_NORMAL));
            fwdRegister.setRegisterTime(new Date());
            fwdRegister.setRetryCount(1);
            fwdRegister.setRetryLimit(5);
            fwdRegisterMapper.insertSelective(fwdRegister);
            log.info("current id:{}", fwdRegister.getId());
        }
    }

    @RequestMapping("/addTimeout")
    public void addTimeout(int num) {
        Long orderId = System.currentTimeMillis() + RandomUtils.nextInt(0, 10000);
        Date date = new Date();
        for (int i = 0; i < num; i++) {
            TtoInf ttoInf = new TtoInf();
            ttoInf.setBizType("00200");
            ttoInf.setCallClass("com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.HelloDubboService");
            ttoInf.setCallMethod("testFwd");
            ttoInf.setCreateTime(new Date());
            ttoInf.setExcuteTime(DateUtil.plusDateBySecond(date, 30));
            ttoInf.setExpiredTime("30");
            ttoInf.setLastUpdateTime(date);
            ttoInf.setReference(orderId);
            ttoInf.setRegisterTime(date);
            ttoInf.setTtoStatus("0");
            ttoInf.setTtoType("0");
            ttoInfMapper.insertSelective(ttoInf);
        }
    }

    @RequestMapping("/testPage")
    public PageInfo<FwdDetail> testPage(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<FwdDetail> fwdDetailList = fwdDetailMapper.selectAll();
        return new PageInfo<>(fwdDetailList);
    }

    @RequestMapping("/testPage2")
    public List<FwdDetail> testPage2(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        RowBounds rowBounds=new RowBounds(getOffset(pageSize,pageNo).intValue(),pageSize);
        List<FwdDetail> fwdDetailList = fwdDetailMapper.selectByRowBounds(null,rowBounds);
        return fwdDetailList;
    }

    public Long getOffset(int limit, int currentPage) {
        return (long) (limit * (currentPage - 1));
    }
}
