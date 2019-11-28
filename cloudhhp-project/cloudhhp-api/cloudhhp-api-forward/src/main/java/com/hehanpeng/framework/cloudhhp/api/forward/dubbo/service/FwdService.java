package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.service;

import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.CancelFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterFwdOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.UpdateFwdInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base.BaseOutputBean;

/**
 * @author hehanpeng
 */
public interface FwdService {

    public RegisterFwdOutputBean registerFwdRegisterService(RegisterFwdInputBean input);

    public BaseOutputBean updateFwdRegisterService(UpdateFwdInputBean input);

    public BaseOutputBean cancelFwdRegisterService(CancelFwdInputBean input);
}
