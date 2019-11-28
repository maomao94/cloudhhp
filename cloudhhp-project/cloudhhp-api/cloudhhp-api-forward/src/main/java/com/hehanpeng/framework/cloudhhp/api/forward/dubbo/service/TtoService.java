package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.service;

import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.CancelTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.RegisterTtoOutputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.UpdateTtoInputBean;
import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base.BaseOutputBean;

/**
 * @author hehanpeng
 */
public interface TtoService {

    public RegisterTtoOutputBean registerTtoInfoService(RegisterTtoInputBean input);

    public BaseOutputBean updateTtoInfoService(UpdateTtoInputBean input);

    public BaseOutputBean cancelTtoInfoService(CancelTtoInputBean input);
}
