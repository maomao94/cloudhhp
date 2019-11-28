package com.hehanpeng.framework.cloudhhp.module.forward.service;

import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.FwdRegister;

public interface AsyncDealFwdRegisterStoreForwardService {
    void execute(FwdRegister input);
}
