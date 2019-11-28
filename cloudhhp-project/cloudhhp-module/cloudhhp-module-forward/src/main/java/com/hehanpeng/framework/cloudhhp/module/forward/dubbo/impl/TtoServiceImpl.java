package com.hehanpeng.framework.cloudhhp.module.forward.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.CancelTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterTtoOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.UpdateTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base.BaseOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.constant.ForwardConstant;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.service.TtoService;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardErrEnum;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.TtoInfMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.TtoInf;
import com.hehanpeng.framework.cloudhhp.module.forward.util.DateUtil;
import com.hehanpeng.framework.cloudhhp.module.forward.util.ObjectValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service(version = "${demo.service.version}")
public class TtoServiceImpl implements TtoService {

    private int waitTime = 2000;

    private int leaseTime = 2000;

    @Autowired(required = false)
    private TtoInfMapper ttoInfMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public RegisterTtoOutputBean registerTtoInfoService(RegisterTtoInputBean input) {
        log.debug(">>>registerTtoInfoService start>>>");
        String errorMessage = validateParams(input);
        RegisterTtoOutputBean output = new RegisterTtoOutputBean();
        if (!"success".equalsIgnoreCase(errorMessage)) {
            log.info(errorMessage);
            output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
            output.setResponseMsg(errorMessage);
            return output;
        }
        TtoInf ttoInf = new TtoInf();
        BeanUtils.copyProperties(input, ttoInf);
        ttoInf.setExcuteTime(DateUtil.plusDateByMilliSecond(input.getRegisterTime(), Integer.parseInt(input.getExpiredTime().trim())));
        int result = ttoInfMapper.insertSelective(ttoInf);
        if (result == 1) {
            output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
            output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
            output.setTtoid(ttoInf.getTtoid());
            log.info(">>>registerTtoInfoService insert TTO_INF {}条记录,ttoid={}>>>", result, ttoInf.getTtoid());
        } else {
            log.info(">>>registerTtoInfoService insert TTO_INF {}条记录,ttoid={}>>>", result, ttoInf.getTtoid());
            log.info(">>>registerTtoInfoService insert TTO_INF fail.>>>");
            output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
            output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
            return output;
        }
        log.debug(">>>registerTtoInfoService end>>>");
        return output;
    }

    @Override
    @Transactional
    public BaseOutputBean updateTtoInfoService(UpdateTtoInputBean input) {
        log.debug(">>>updateTtoInfoService start>>>");
        String errorMessage = updateTtoInfoServiceValidateParams(input);
        BaseOutputBean output = new BaseOutputBean();
        if (!"success".equalsIgnoreCase(errorMessage)) {
            log.info(errorMessage);
            output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
            output.setResponseMsg(errorMessage);
            return output;
        }
        String key = "TtoForward_ID_" + input.getTtoid();
        RLock rlock = redissonClient.getLock(key);
        try {
            boolean flag = rlock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (!flag) {
                output.setResponseCode(ForwardErrEnum.ERR_0006.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0006.getMessage());
                return output;
            }

            TtoInf ttoInf = ttoInfMapper.selectByPrimaryKey(input.getTtoid());
            if (ttoInf == null) {
                output.setResponseCode(ForwardErrEnum.ERR_0004.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0004.getMessage());
                return output;
            }
            ttoInf.setExcuteTime(DateUtil.plusDateByMilliSecond(input.getUppSysTime(), Integer.parseInt(input.getExpiredTime().trim())));
            ttoInf.setExpiredTime(input.getExpiredTime());
            ttoInf.setTtoStatus(ForwardParamConstant.TTOINF_STATUS_UNDEAL);
            int result = ttoInfMapper.updateByPrimaryKeySelective(ttoInf);
            if (result == 1) {
                output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
                output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
                log.info(">>>updateTtoInfoService update TTO_INF {}条记录,ttoid={}>>>", result, input.getTtoid());
                return output;
            } else {
                log.info(">>>updateTtoInfoService update TTO_INF {}条记录,ttoid={}>>>", result, input.getTtoid());
                log.info(">>>updateTtoInfoService update TTO_INF fail.>>>");
                output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
                return output;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("TtoProviderImpl-updateTtoInfoService-Throwable: ", e);
            output.setResponseCode(ForwardErrEnum.ERR_0001.getCode());
            output.setResponseMsg(ForwardErrEnum.ERR_0001.getMessage());
        } finally {
            rlock.unlock();
        }
        return output;
    }

    @Override
    @Transactional
    public BaseOutputBean cancelTtoInfoService(CancelTtoInputBean input) {
        log.debug(">>>cancelTtoInfo start>>>");
        String errorMessage = cancelTtoInfoServiceValidateParams(input);
        BaseOutputBean output = new BaseOutputBean();
        if (!"success".equalsIgnoreCase(errorMessage)) {
            log.info(errorMessage);
            output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
            output.setResponseMsg(errorMessage);
            return output;
        }

        int waitTime = 2000;
        String key = "TtoForward_ID_" + input.getTtoid();
        RLock rlock = redissonClient.getLock(key);
        try {
            boolean flag = rlock.tryLock(waitTime, waitTime, TimeUnit.MILLISECONDS);
            if (!flag) {
                log.info(">>>cancelTtoInfoService 获取锁失败，key={}>>>>", key);
                output.setResponseCode(ForwardErrEnum.ERR_0006.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0006.getMessage());
                return output;
            }

            TtoInf ttoInf = ttoInfMapper.selectByPrimaryKey(input.getTtoid());
            if (ttoInf == null) {
                output.setResponseCode(ForwardErrEnum.ERR_0004.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0004.getMessage());
                return output;
            }
            ttoInf.setTtoStatus(input.getTtoStatus());

            int result = ttoInfMapper.updateByPrimaryKeySelective(ttoInf);
            if (result == 1) {
                output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
                output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
                log.info(">>>cancelTtoInfoService update TTO_INF {}条记录,ttoid={}>>>", result, input.getTtoid());
                return output;
            } else {
                log.info(">>>cancelTtoInfoService update TTO_INF {}条记录,ttoid={}>>>", result, input.getTtoid());
                log.info(">>>cancelTtoInfoService update TTO_INF fail.>>>");
                output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
                output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
                return output;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("TtoProviderImpl-cancelTtoInfoService-Throwable: ", e);
            output.setResponseCode(ForwardErrEnum.ERR_0001.getCode());
            output.setResponseMsg(ForwardErrEnum.ERR_0001.getMessage());
        } finally {
            rlock.unlock();
        }
        return output;
    }

    /**
     * 验证注册服务的参数合法性
     *
     * @param input
     * @return
     */

    private String validateParams(RegisterTtoInputBean input) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;

        // 验证请求参数有效性（必选项）

        if (ObjectValidUtil.isInvalid(input.getReference())) {
            errorMessage = "request params[reference] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getRegisterTime())) {
            errorMessage = "request params[registerTime] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getTtoType())) {
            errorMessage = "request params[type] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getTtoStatus())) {
            errorMessage = "request params[status] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getBizType())) {
            errorMessage = "request params[bizType] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getExpiredTime())) {
            errorMessage = "request params[expiredTime] error.";
            return errorMessage;
        }

        if (input.getBizType().equals(ForwardParamConstant.TTOINF_BIZTYPE_DEFAULT)) {
            if (ObjectValidUtil.isInvalid(input.getCallClass())) {
                errorMessage = "request params[callClass] error.";
                return errorMessage;
            }
            if (ObjectValidUtil.isInvalid(input.getCallMethod())) {
                errorMessage = "request params[callMethod] error.";
                return errorMessage;
            }
        }
        return "success";
    }

    private String updateTtoInfoServiceValidateParams(UpdateTtoInputBean input) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;

        // 验证请求参数有效性（必选项）

        if (ObjectValidUtil.isInvalid(input.getTtoid())) {
            errorMessage = "request params[ttoid] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getExpiredTime())) {
            errorMessage = "request params[expiredTime] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getUppSysTime())) {
            errorMessage = "request params[uppSysTime] error.";
            return errorMessage;
        }
        return "success";
    }

    private String cancelTtoInfoServiceValidateParams(CancelTtoInputBean input) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;

        // 验证请求参数有效性（必选项）
        if (ObjectValidUtil.isInvalid(input.getTtoid())) {
            errorMessage = "request params[ttoid] error.";
            return errorMessage;
        }
        if (ObjectValidUtil.isInvalid(input.getTtoStatus())) {
            errorMessage = "request params[status] error.";
            return errorMessage;
        }
        if (input.getTtoStatus().equals(ForwardParamConstant.TTOINF_STATUS_UNDEAL)) {
            errorMessage = "request params[status] error.";
            return errorMessage;
        }
        return "success";
    }
}
