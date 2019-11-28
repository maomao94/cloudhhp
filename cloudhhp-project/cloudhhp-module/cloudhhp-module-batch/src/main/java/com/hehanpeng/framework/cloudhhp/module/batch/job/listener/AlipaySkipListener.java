package com.hehanpeng.framework.cloudhhp.module.batch.job.listener;

import com.hehanpeng.framework.cloudhhp.module.batch.domain.dto.AlipayTranDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlipaySkipListener implements SkipListener<AlipayTranDO, AlipayTranDO> {

    @Override
    public void onSkipInProcess(AlipayTranDO alipayTranDO, Throwable throwable) {
        log.error("AlipayTran was skipped in process: {}", alipayTranDO, throwable);
    }

    @Override
    public void onSkipInRead(Throwable throwable) {
        log.error("AlipayTran onSkipInRead ", throwable);
    }

    @Override
    public void onSkipInWrite(AlipayTranDO alipayTranDO, Throwable throwable) {
        log.error("AlipayTran was skipped in process: {}", alipayTranDO, throwable);
    }

}