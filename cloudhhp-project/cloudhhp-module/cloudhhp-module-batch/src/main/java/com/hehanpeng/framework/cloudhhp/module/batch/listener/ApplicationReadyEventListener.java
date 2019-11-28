package com.hehanpeng.framework.cloudhhp.module.batch.listener;

import com.hehanpeng.framework.cloudhhp.common.dicts.BatchConstants;
import com.hehanpeng.framework.cloudhhp.module.batch.dao.batch.BatchTaskInfMapper;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch.BatchTaskInf;
import com.hehanpeng.framework.cloudhhp.module.batch.factory.CronTriggerFactory;
import com.hehanpeng.framework.cloudhhp.module.batch.quartz.AbstractQuartzJobBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired(required = false)
    BatchTaskInfMapper batchTaskInfMapper;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("start ApplicationReadyEvent initialized...");
        ConfigurableApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        CronTriggerFactory cronTriggerFactory = applicationContext.getBean(CronTriggerFactory.class);
        String clusterName = nacosDiscoveryProperties.getClusterName();
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String machId = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        Example example = new Example(BatchTaskInf.class);
        example.createCriteria().andEqualTo("clusterName", clusterName)
                .andEqualTo("machId", machId)
                .andEqualTo("taskStat", "01");
        List<BatchTaskInf> batchTaskInfs = batchTaskInfMapper.selectByExample(example);
        for (BatchTaskInf entity : batchTaskInfs) {
            String jobName = entity.getTaskName();
            String expression = entity.getTaskExpress();
            AbstractQuartzJobBean jobBean = (AbstractQuartzJobBean) applicationContext.getBean(jobName.trim() + BatchConstants.QUARTZ_JOB_BEAN_SUFFIX);
            if (jobBean != null) {
                cronTriggerFactory.createTimingTask(expression, jobName, jobBean.getClass());
            }
        }
    }
}
