package com.hehanpeng.framework.cloudhhp.module.forward.service;

import com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward.TtoInf;

/**
 * @author hehanpeng
 * 2018/9/17 15:58
 */
public interface AsyncDealTtoInfoTimeOutService {
    void execute(TtoInf input);
}
