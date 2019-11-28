package com.hehanpeng.framework.cloudhhp.module.batch.quartz;

import com.hehanpeng.framework.cloudhhp.module.batch.util.DateUtil;
import com.hehanpeng.framework.cloudhhp.module.batch.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public abstract class AbstractQuartzJobBean extends QuartzJobBean {

    protected abstract String getJobName();

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        try {
            Job job = (Job) SpringUtils.getBeanObj(getJobName());
            JobLauncher jobLauncher = (JobLauncher) SpringUtils.getBeanObj("jobLauncher");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", DateTime.now().minusDays(1).toString(DateUtil.DATE_FORMAT_YYYY_MM_DD)).toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("JobExecutionAlreadyRunningException error:{}", e);
        } catch (JobRestartException e) {
            log.error("JobRestartException error:{}", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("JobInstanceAlreadyCompleteException error:{}", e);
        } catch (JobParametersInvalidException e) {
            log.error("JobParametersInvalidException error:{}", e);
        } catch (Exception e) {
            log.error("exception:{}", e);
        }
    }
}
