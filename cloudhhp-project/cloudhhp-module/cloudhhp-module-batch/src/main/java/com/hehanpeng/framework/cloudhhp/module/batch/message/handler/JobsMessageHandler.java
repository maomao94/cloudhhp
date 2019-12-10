package com.hehanpeng.framework.cloudhhp.module.batch.message.handler;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSON;
import com.hehanpeng.framework.cloudhhp.common.dicts.BatchChannelConstant;
import com.hehanpeng.framework.cloudhhp.module.batch.dao.batch.BatchJobInstanceMapper;
import com.hehanpeng.framework.cloudhhp.module.batch.dao.batch.extend.BatchJobExecutionParamsExtendMapper;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch.BatchJobExecutionParams;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch.BatchJobInstance;
import com.hehanpeng.framework.cloudhhp.module.batch.message.Message;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JobsMessageHandler implements MessageHandler {

    @Autowired(required = false)
    private BatchJobExecutionParamsExtendMapper batchJobExecutionParamsExtendMapper;

    @Autowired(required = false)
    private BatchJobInstanceMapper batchJobInstanceMapper;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public String getChannelName() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String machId = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        log.info("监听channel:{}", BatchChannelConstant.CHANNEL_JOBS + machId);
        return BatchChannelConstant.CHANNEL_JOBS + "-" + machId;
    }

    @Override
    public void processMessage(Message message) {
        log.info("JobsMessageHandler 接收消息:{}", message);
        Map<String, Object> map = (Map<String, Object>) message.getContent();
        String type = (String) map.get("type");
        String jobId = (String) map.get("jobId");
        String jobExcId = (String) map.get("jobExcId");

        BatchJobInstance instance = batchJobInstanceMapper.selectByPrimaryKey(Long.parseLong(jobId));
        log.info("JobsMessageHandler:{}", JSON.toJSONString(instance));
        Job job = (Job) context.getBean(instance.getJobName());
        JobParametersBuilder jobParameters = new JobParametersBuilder();
        Map<String, Object> mp = new HashMap<String, Object>(2);
        mp.put("jobId", jobId);
        mp.put("jobExcId", jobExcId);
        List<BatchJobExecutionParams> batchJobExecutionParams = batchJobExecutionParamsExtendMapper.selectByJobId(mp);
        if (batchJobExecutionParams.isEmpty()) {
            log.error("job ExecutionParams is empty");
            return;
        }
        for (BatchJobExecutionParams para : batchJobExecutionParams) {
            jobParameters.addString(para.getKeyName(), para.getStringVal());
        }
        if ("restart".equals(type)) {
            jobParameters.addString("timestamp", String.valueOf(System.currentTimeMillis()));
        }
        try {
            jobLauncher.run(job, jobParameters.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("job error:{}", e);
        } catch (JobRestartException e) {
            log.error("job error:{}", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("job error:{}", e);
        } catch (JobParametersInvalidException e) {
            log.error("job error:{}", e);
        }
        log.info("接收消息:{}", message);
    }

}
