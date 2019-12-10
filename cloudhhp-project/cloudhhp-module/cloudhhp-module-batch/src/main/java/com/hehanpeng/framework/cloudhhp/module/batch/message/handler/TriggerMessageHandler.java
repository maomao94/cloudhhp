package com.hehanpeng.framework.cloudhhp.module.batch.message.handler;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.hehanpeng.framework.cloudhhp.common.dicts.BatchChannelConstant;
import com.hehanpeng.framework.cloudhhp.common.dicts.BatchConstants;
import com.hehanpeng.framework.cloudhhp.module.batch.dao.batch.BatchTaskInfMapper;
import com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch.BatchTaskInf;
import com.hehanpeng.framework.cloudhhp.module.batch.factory.CronTriggerFactory;
import com.hehanpeng.framework.cloudhhp.module.batch.message.Message;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageHandler;
import com.hehanpeng.framework.cloudhhp.module.batch.quartz.AbstractQuartzJobBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class TriggerMessageHandler implements MessageHandler {

    private static final String TASK_STOP_STATUS = "00";

    private static final String TASK_START_STATUS = "01";

    @Autowired
    private CronTriggerFactory cronTriggerFactory;

    @Autowired(required = false)
    private BatchTaskInfMapper batchTaskInfMapper;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private ApplicationContext context;

    @Override
    public String getChannelName() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        String machId = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        log.info("监听channel:{}", BatchChannelConstant.CHANNEL_TRIGGER + machId);
        return BatchChannelConstant.CHANNEL_TRIGGER + "-" + machId;
    }

    @Override
    public void processMessage(Message message) {
        log.info("TriggerMessageHandler 接收消息:{}", message);
        Map<String, Object> map = (Map<String, Object>) message.getContent();
        String jobName = (String) map.get("jobName");
        String expression = (String) map.get("expression");
        String status = (String) map.get("status");
        if (TASK_STOP_STATUS.equals(status)) {
            boolean flag = cronTriggerFactory.stopTimingTask(jobName);
            if (flag) {
                Example example = new Example(BatchTaskInf.class);
                example.createCriteria().andEqualTo("taskName", jobName);
                BatchTaskInf batchTaskInf = batchTaskInfMapper.selectOneByExample(example);
                if (batchTaskInf != null) {
                    batchTaskInf.setUpdateTime(new Date());
                    batchTaskInf.setTaskStat(TASK_STOP_STATUS);
                    batchTaskInfMapper.updateByPrimaryKey(batchTaskInf);
                }
            }
        } else if (TASK_START_STATUS.equals(status)) {
            AbstractQuartzJobBean jobBean = (AbstractQuartzJobBean) context.getBean(jobName.trim() + BatchConstants.QUARTZ_JOB_BEAN_SUFFIX);
            if (jobBean != null) {
                boolean flag = cronTriggerFactory.createTimingTask(expression, jobName, jobBean.getClass());
                if (flag) {
                    Example example = new Example(BatchTaskInf.class);
                    example.createCriteria().andEqualTo("taskName", jobName);
                    BatchTaskInf batchTaskInf = batchTaskInfMapper.selectOneByExample(example);
                    if (batchTaskInf != null) {
                        batchTaskInf.setUpdateTime(new Date());
                        batchTaskInf.setTaskStat(TASK_START_STATUS);
                        batchTaskInfMapper.updateByPrimaryKey(batchTaskInf);
                    }
                }
            }
        }
    }
}
