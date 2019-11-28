package com.hehanpeng.framework.cloudhhp.module.batch.quartz;

import org.springframework.stereotype.Component;

@Component
public class TestJobQuartzJobBean extends AbstractQuartzJobBean {
    @Override
    protected String getJobName() {
        return "testJob";
    }
}
