package com.hehanpeng.framework.cloudhhp.module.batch.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 单例 注意时间戳线程安全问题
 */
@Service
@Slf4j
public class AlipayJobListener implements JobExecutionListener {
    long startTime;

    long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("AlipayJobListener begin>>>>>>>>>");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        for (StepExecution bean : stepExecutions) {
            log.info("<<<<<<<<<<<<<<<<<<<<StepExecution [stepName:{},status:{}] end", bean.getStepName(), bean.getStatus());
        }
        log.info("<<<<<<<<<<<<<<<<<<<<AlipayJobListener [exId:{},id:{},jobName:{},status:{}] end"
                ,jobExecution.getJobId(),jobExecution.getJobInstance().getInstanceId(),jobExecution.getJobInstance().getJobName(),jobExecution.getStatus());
        log.info("<<<<<<<<<<<<<<<<<<<<耗时：" + ((endTime - startTime) + "ms"));
    }
}