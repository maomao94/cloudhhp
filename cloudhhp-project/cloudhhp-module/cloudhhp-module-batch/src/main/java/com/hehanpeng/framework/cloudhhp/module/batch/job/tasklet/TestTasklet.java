package com.hehanpeng.framework.cloudhhp.module.batch.job.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TestTasklet implements Tasklet {

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("TestTasklet start");
        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
        String date = String.valueOf(jobParameters.get("date"));
        String time = String.valueOf(jobParameters.get("time"));
        log.info("TestTasklet date:{},time:{}",date,time);
        log.info("TestTasklet end");
        return RepeatStatus.FINISHED;
    }
}

