package com.hehanpeng.framework.cloudhhp.module.batch.factory;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class CronTriggerFactory {

    @Autowired(required = false)
    private SchedulerFactoryBean schedulerFactoryBean;

    private static final String DEFAULT_TASK_PREFIX = "TASK-";

    private static final String DEFAULT_JOB_PREFIX = "JOB-";

    private static final String DEFAULT_TASKID = "taskId";

    /**
     * 添加或修改一个定时任务
     */
    public boolean createTimingTask(String expression, String taskId, Class<? extends QuartzJobBean> cls) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(DEFAULT_TASK_PREFIX + taskId, DEFAULT_JOB_PREFIX + taskId);
            // 不存在，创建一个
            JobKey jobKey = new JobKey(DEFAULT_TASK_PREFIX + taskId, DEFAULT_JOB_PREFIX + taskId);
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobKey).build();
            // 稽核任务基础信息
            jobDetail.getJobDataMap().put(DEFAULT_TASKID, taskId);
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(expression);
            // 按cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(new Date()).withSchedule(cronScheduleBuilder).build();
            // 加入任务队列
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(triggerKey)) {
                stopTimingTask(taskId);
            }
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("启动定时任务taskId:{}", taskId);
            return true;
        } catch (SchedulerException e) {
            log.error("create timing task:{} error:{}", taskId, e);
        }
        return false;
    }


    public boolean stopTimingTask(String taskId) {
        log.info("停止定时任务: taskId:{}", taskId);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(DEFAULT_TASK_PREFIX + taskId, DEFAULT_JOB_PREFIX + taskId);
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(new JobKey(DEFAULT_TASK_PREFIX + taskId, DEFAULT_JOB_PREFIX + taskId));// 删除任务
            return true;
        } catch (SchedulerException e) {
            log.error("stop timging task key:{} error:{}", taskId, e);
        }// 停止触发器
        return false;
    }
}
