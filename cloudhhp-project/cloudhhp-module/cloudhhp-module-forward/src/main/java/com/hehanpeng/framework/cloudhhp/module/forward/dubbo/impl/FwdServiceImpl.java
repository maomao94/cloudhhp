package com.hehanpeng.framework.cloudhhp.module.forward.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.CancelFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterFwdOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.UpdateFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base.BaseOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.constant.ForwardConstant;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.service.FwdService;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardErrEnum;
import com.hehanpeng.framework.cloudhhp.module.forward.constant.ForwardParamConstant;
import com.hehanpeng.framework.cloudhhp.module.forward.dao.forward.FwdRegisterMapper;
import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdRegister;
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
public class FwdServiceImpl implements FwdService {
	
	private int waitTime = 2000;
	
	private int leaseTime = 2000;

    @Autowired
    private FwdRegisterMapper fwdRegisterMapper;

    @Autowired
    private RedissonClient redissonClient;
    

    @Override
    @Transactional
	public RegisterFwdOutputBean registerFwdRegisterService(RegisterFwdInputBean input) {
		log.debug("存储转发注册，开始......");
		String errorMessage = validateParams(input);
		RegisterFwdOutputBean output = new RegisterFwdOutputBean();
		if (!"success".equalsIgnoreCase(errorMessage)) {
			log.info(errorMessage);
			output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
			output.setResponseMsg(errorMessage);
			return output;
		}
		try {
			FwdRegister fwdRegister= new FwdRegister();
			BeanUtils.copyProperties(input, fwdRegister);
			if (input.getRetryLimit() > 0) {
				fwdRegister.setRetryCount(0);
				fwdRegister.setNextActiveTime(DateUtil.calcNextActiveTime(input.getRetryLimit(), 0,
						input.getBaseInterval(), input.getIntervalDelta(), input.getFwdType()));
				fwdRegister.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_WAIT);
			} else {
				log.info(">>>registerFwdRegisterService retryLimit erro>>>");
				output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0002.getMessage());
				return output;
			}
			int result = fwdRegisterMapper.insertSelective(fwdRegister);
			if (result == 1) {
				output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
				output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
				output.setId(fwdRegister.getId());
				log.info(">>>registerFwdRegisterService insert FWD_REGISTER {}条记录,id={}>>>", result,
						fwdRegister.getId());
			} else {
				log.info(">>>registerFwdRegisterService insert FWD_REGISTER {}条记录,id={}>>>", result,
						fwdRegister.getId());
				log.info(">>>registerFwdRegisterService insert FWD_REGISTER fail.>>>");
				output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
				return output;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FwdProviderImpl-registerFwdRegisterService-Throwable: ", e);
			output.setResponseCode(ForwardErrEnum.ERR_0001.getCode());
			output.setResponseMsg(ForwardErrEnum.ERR_0001.getMessage());
		}

		log.debug(">>>registerFwdRegisterService end>>>");
		return output;
	}

    @Override
    @Transactional
	public BaseOutputBean updateFwdRegisterService(UpdateFwdInputBean input) {
		log.debug(">>>updateFwdRegisterService start>>>");
		String errorMessage = updateFwdRegisterServiceValidateParams(input);
		BaseOutputBean output = new BaseOutputBean();
		if (!"success".equalsIgnoreCase(errorMessage)) {
			log.info(errorMessage);
			output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
			output.setResponseMsg(errorMessage);
			return output;
		}
		
		String key = "StoreForward_ID_" + input.getId();
		RLock rlock = redissonClient.getLock(key);
		try {
			boolean flag = rlock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
			if (!flag) {
				output.setResponseCode(ForwardErrEnum.ERR_0006.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0006.getMessage());
				return output;
			}
			FwdRegister fwdRegister = fwdRegisterMapper.selectByPrimaryKey(input.getId());
			if (fwdRegister == null) {
				output.setResponseCode(ForwardErrEnum.ERR_0004.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0004.getMessage());
				return output;
			}
			fwdRegister.setNextActiveTime(input.getNextActiveTime());
			fwdRegister.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_WAIT);
			int result = fwdRegisterMapper.updateByPrimaryKeySelective(fwdRegister);
			if (result == 1) {
				output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
				output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
				log.info(">>>updateFwdRegisterService update FWD_REGISTER {}条记录,id={}>>>", result, input.getId());
			} else {
				log.info(">>>updateFwdRegisterService update FWD_REGISTER {}条记录,id={}>>>", result, input.getId());
				log.info(">>>updateFwdRegisterService update FWD_REGISTER fail.>>>");
				output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
				return output;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("FwdProviderImpl-updateFwdRegisterService-Throwable: ", e);
			output.setResponseCode(ForwardErrEnum.ERR_0001.getCode());
			output.setResponseMsg(ForwardErrEnum.ERR_0001.getMessage());
		} finally {
			rlock.unlock();
		}
		return output;
	}

    @Override
    @Transactional
	public BaseOutputBean cancelFwdRegisterService(CancelFwdInputBean input) {
		log.debug(">>>cancelFwdRegister start>>>");
		String errorMessage = cancelFwdRegisterServiceValidateParams(input);
		BaseOutputBean output = new BaseOutputBean();
		if (!"success".equalsIgnoreCase(errorMessage)) {
			log.info(errorMessage);
			output.setResponseCode(ForwardErrEnum.ERR_0002.getCode());
			output.setResponseMsg(errorMessage);
			return output;
		}

		int waitTime = 2000;
		String key = "StoreForward_ID_" + input.getId();
		RLock rlock = redissonClient.getLock(key);
		try {
			boolean flag = rlock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
			if (!flag) {
				log.info(">>>cancelFwdRegisterService 获取锁失败，key={}>>>>", key);
				output.setResponseCode(ForwardErrEnum.ERR_0006.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0006.getMessage());
				return output;
			}

			FwdRegister fwdRegister = fwdRegisterMapper.selectByPrimaryKey(input.getId());
			if (fwdRegister == null) {
				output.setResponseCode(ForwardErrEnum.ERR_0004.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0004.getMessage());
				return output;
			}

			fwdRegister.setFwdStatus(ForwardParamConstant.FWDREG_STATUS_CANCEL);
			int result = fwdRegisterMapper.updateByPrimaryKeySelective(fwdRegister);
			if (result == 1) {
				output.setResponseCode(ForwardConstant.RETURN_CODE_SUCCESS);
				output.setResponseMsg(ForwardConstant.RETURN_MSG_SUCCESS);
				log.info(">>>cancelFwdRegisterService update FWD_REGISTER {}条记录,id={}>>>", result, input.getId());
				FwdRegister register = fwdRegisterMapper.selectByPrimaryKey(input.getId());
				log.info("cancelFwdRegisterService after cancel, now register={}", register);
			} else {
				log.info(">>>cancelFwdRegisterService update FWD_REGISTER {}条记录,id={}>>>", result, input.getId());
				log.info(">>>cancelFwdRegisterService update FWD_REGISTER fail.>>>");
				output.setResponseCode(ForwardErrEnum.ERR_0003.getCode());
				output.setResponseMsg(ForwardErrEnum.ERR_0003.getMessage());
				return output;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("FwdProviderImpl-cancelFwdRegisterService-Throwable: ", e);
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

	private String validateParams(RegisterFwdInputBean input) {
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
		if (ObjectValidUtil.isInvalid(input.getFwdType())) {
			errorMessage = "request params[fwdType] error.";
			return errorMessage;
		}
		if (ObjectValidUtil.isInvalid(input.getFwdStatus())) {
			errorMessage = "request params[fwdStatus] error.";
			return errorMessage;
		}
		if (ObjectValidUtil.isInvalid(input.getRetryLimit())) {
			errorMessage = "request params[retryLimit] error.";
			return errorMessage;
		}
		if (ObjectValidUtil.isInvalid(input.getBaseInterval())) {
			errorMessage = "request params[baseInterval] error.";
			return errorMessage;
		}
		if (ObjectValidUtil.isInvalid(input.getIntervalDelta())) {
			errorMessage = "request params[intervalDelta] error.";
			return errorMessage;
		}

		if (ObjectValidUtil.isInvalid(input.getCallClass())) {
			errorMessage = "request params[callClass] error.";
			return errorMessage;
		}
		if (ObjectValidUtil.isInvalid(input.getCallMethod())) {
			errorMessage = "request params[callMethod] error.";
			return errorMessage;
		}
		return "success";
	}

    private String updateFwdRegisterServiceValidateParams(UpdateFwdInputBean input) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;

        // 验证请求参数有效性（必选项）

        if (ObjectValidUtil.isInvalid(input.getId())) {
            errorMessage = "request params[id] error.";
            return errorMessage;
        }
        return "success";
    }

    private String cancelFwdRegisterServiceValidateParams(CancelFwdInputBean input) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;

        // 验证请求参数有效性（必选项）
        if (ObjectValidUtil.isInvalid(input.getId())) {
            errorMessage = "request params[id] error.";
            return errorMessage;
        }
       
        return "success";
    }
}
