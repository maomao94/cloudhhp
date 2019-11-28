package com.hehanpeng.framework.cloudhhp.module.batch.job.job;

import com.hehanpeng.framework.cloudhhp.module.batch.domain.dto.AlipayTranDO;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.dto.HopPayTranDO;
import com.hehanpeng.framework.cloudhhp.module.batch.job.listener.AlipayJobListener;
import com.hehanpeng.framework.cloudhhp.module.batch.job.listener.AlipaySkipListener;
import com.hehanpeng.framework.cloudhhp.module.batch.job.listener.TestStepListener;
import com.hehanpeng.framework.cloudhhp.module.batch.job.processor.AlipayItemProcessor;
import com.hehanpeng.framework.cloudhhp.module.batch.job.processor.AlipayValidateProcessor;
import com.hehanpeng.framework.cloudhhp.module.batch.job.reader.AlipayFileItemReader;
import com.hehanpeng.framework.cloudhhp.module.batch.job.writer.AlipayDBItemWriter;
import com.hehanpeng.framework.cloudhhp.module.batch.job.writer.AlipayFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class AlipayFileJobConfig {
    @Autowired
    public JobLauncher jobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AlipayFileItemReader alipayFileItemReader;

    @Autowired
    private AlipayItemProcessor alipayItemProcessor;

    @Autowired
    private AlipayFileItemWriter alipayFileItemWriter;

    @Autowired
    private AlipayDBItemWriter alipayDBItemWriter;

    @Autowired
    private AlipaySkipListener listener;

    @Autowired
    private TestStepListener testStepListener;

    @Autowired
    private AlipayJobListener alipayJobListener;

    @Bean
    public Job alipayFileJob() {
        return jobBuilderFactory.get("alipayFileJob")
                .incrementer(new RunIdIncrementer())
                .start(step2()).listener(alipayJobListener).build();
//                .start(step1())
//                .on(ExitStatus.COMPLETED.getExitCode())
//                .to(splitFlow()).end()
//                .build();
    }

    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow1(), flow2())
                .build();
    }

    public Flow flow1() {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(step2())
                .build();
    }

    public Flow flow2() {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(step3())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .processor(alipayItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .build();
    }


    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<AlipayTranDO, AlipayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .writer(alipayDBItemWriter)
//                .faultTolerant()
//                .skipLimit(20)
//                .skip(Exception.class)
//                .listener(listener)
//                .retryLimit(3)
//                .retry(Exception.class)
                .listener(testStepListener)
//                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step3() {
        CompositeItemProcessor<AlipayTranDO, HopPayTranDO> compositeItemProcessor = new CompositeItemProcessor<AlipayTranDO, HopPayTranDO>();
        List compositeProcessors = new ArrayList();
        compositeProcessors.add(new AlipayValidateProcessor());
        compositeProcessors.add(new AlipayItemProcessor());
        compositeItemProcessor.setDelegates(compositeProcessors);
        return stepBuilderFactory.get("step3")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .processor(compositeItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(16);
        taskExecutor.setMaxPoolSize(16);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .processor(alipayItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(4)
                .build();
    }

}
