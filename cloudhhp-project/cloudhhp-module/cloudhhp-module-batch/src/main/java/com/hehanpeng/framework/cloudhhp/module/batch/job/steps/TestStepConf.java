package com.hehanpeng.framework.cloudhhp.module.batch.job.steps;

import com.hehanpeng.framework.cloudhhp.module.batch.job.listener.TestStepListener;
import com.hehanpeng.framework.cloudhhp.module.batch.job.tasklet.TestTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TestStepConf {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TestTasklet testTasklet;

    @Autowired
    private TestStepListener testStepListener;

    @Bean
    public Step testStep() {
        return stepBuilderFactory.get("测试step")
                .tasklet(testTasklet)
                .listener(testStepListener)
                .build();
    }
}
