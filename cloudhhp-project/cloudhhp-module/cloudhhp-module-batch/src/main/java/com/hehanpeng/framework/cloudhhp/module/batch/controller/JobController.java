package com.hehanpeng.framework.cloudhhp.module.batch.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.hehanpeng.framework.cloudhhp.module.batch.factory.CronTriggerFactory;
import com.hehanpeng.framework.cloudhhp.module.batch.message.MessageListenerInitilization;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageSender;
import com.hehanpeng.framework.cloudhhp.module.batch.quartz.TestJobQuartzJobBean;
import com.hehanpeng.framework.cloudhhp.module.batch.util.DateUtil;
import com.hehanpeng.framework.cloudhhp.module.batch.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@Slf4j
public class JobController {

    @Autowired
    private CronTriggerFactory cronTriggerFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private MessageSender sender;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 重跑job
     *
     * @param jobName
     * @param date
     * @return
     */
    @PostMapping("/restart")
    public boolean jobsRestart(String jobName, String date) {
        Job job = (Job) SpringUtils.getBeanObj(jobName);
        JobParametersBuilder jobParameters = new JobParametersBuilder();
        if (StringUtils.hasText(date)) {
            jobParameters.addString("date", date);
        } else {
            jobParameters.addString("date", DateTime.now().minusDays(1).toString(DateUtil.DATE_FORMAT_YYYY_MM_DD));
        }
        jobParameters.addString("time", DateTime.now().toString(DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        try {
            jobLauncher.run(job, jobParameters.toJobParameters());
            return true;
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("restart job error:{}", e);
        } catch (JobRestartException e) {
            log.error("restart job error:{}", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("restart job error:{}", e);
        } catch (JobParametersInvalidException e) {
            log.error("restart job error:{}", e);
        }
        return false;
    }

    /**
     * 续跑Job
     *
     * @param jobName
     * @param date
     * @return
     */
    @PostMapping("/continue")
    public boolean jobsContinue(String jobName, String date) {
        Job job = (Job) SpringUtils.getBeanObj(jobName);
        JobParametersBuilder jobParameters = new JobParametersBuilder();
        if (StringUtils.hasText(date)) {
            jobParameters.addString("date", date);
        } else {
            jobParameters.addString("date", DateTime.now().minusDays(1).toString(DateUtil.DATE_FORMAT_YYYY_MM_DD));
        }
        try {
            jobLauncher.run(job, jobParameters.toJobParameters());
            return true;
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("continue job error:{}", e);
        } catch (JobRestartException e) {
            log.error("continue job error:{}", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("continue job error:{}", e);
        } catch (JobParametersInvalidException e) {
            log.error("continue job error:{}", e);
        }
        return false;
    }

    /**
     * 创建定时任务
     *
     * @param expression 表达式
     * @param jobName    job名
     * @return
     */
    @PostMapping("/create")
    public boolean createJob(String expression, String jobName) {
        log.info("expression:{},taskId:{}", expression, jobName);
        boolean flag = cronTriggerFactory.createTimingTask(expression, jobName, TestJobQuartzJobBean.class);
        return flag;
    }

    /**
     * 停止定时任务
     *
     * @param jobName job名
     * @return
     */
    @PostMapping("/stop")
    public boolean stopJob(String jobName) {
        log.info("停止定时任务:{}", jobName);
        boolean flag = cronTriggerFactory.stopTimingTask(jobName);
        return flag;
    }

    @PostMapping("/sendMsg/job")
    public void sendJobMessage(String jobId, String jobExcId, String type) {
        Map<String, String> message = new HashMap<>();
        message.put("jobId", jobId);
        message.put("jobExcId", jobExcId);
        message.put("type", type);
        sender.sendMessage("jobs.timed.task-000", message);
    }

    @PostMapping("/sendMsg/trigger")
    public void sendTriggerMessage(String jobName,String expression,String status) {
        Map<String, String> message = new HashMap<>();
        message.put("jobName", jobName);
        message.put("expression", expression);
        message.put("status", status);
        sender.sendMessage("jobs.timed.task-000", message);
    }

    @Autowired
    private MessageListenerInitilization redisMessageInit;

    @PostMapping("/stopReceive")
    public void stopReceiveMessage() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String machId = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        redisMessageInit.stopMessageHandler("jobs.timed.task-" + machId);
    }

    @PostMapping("/startReceive")
    public void startReceiveMessage() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String machId = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        redisMessageInit.startMessageHandler("jobs.timed.task-" + machId);
    }
}
