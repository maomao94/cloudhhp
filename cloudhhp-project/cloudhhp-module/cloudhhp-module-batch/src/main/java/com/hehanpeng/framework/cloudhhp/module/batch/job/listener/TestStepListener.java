package com.hehanpeng.framework.cloudhhp.module.batch.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
/**
 * 单例 注意时间戳线程安全问题
 */
public class TestStepListener implements StepExecutionListener {

	long startTime;
	
	long endTime;

	public void beforeStep(StepExecution stepExecution) {
		startTime = System.currentTimeMillis();
		System.out.println("TestStepListener begin>>>>>>>>>");

	}

	public ExitStatus afterStep(StepExecution stepExecution) {
		endTime = System.currentTimeMillis();
		System.out.println("<<<<<<<<<<<<<<<<<<<<TestStepListener end");
		System.out.println("<<<<<<<<<<<<<<<<<<<<耗时："+((endTime - startTime) + "ms"));
		return ExitStatus.COMPLETED;
	}
}
