package com.wf.ew.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.dao.JobExecutionMapper;
import com.wf.ew.system.model.JobExecution;
import com.wf.ew.system.model.JobExecutionExtend;
import com.wf.ew.system.model.TaskInf;
import com.wf.ew.system.service.JobExecutionService;
import com.wf.ew.system.service.TaskInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-25
 */
@Service
public class JobExecutionServiceImpl extends ServiceImpl<JobExecutionMapper, JobExecution> implements JobExecutionService {
    @Autowired
    TaskInfService taskInfService;

    @Override
    public PageResult<JobExecutionExtend> selectList(PageParam pageParam) {
        String startDate = (String) pageParam.getPageData().get("startDate");
        String endDate = (String) pageParam.getPageData().get("endDate");
        if (!StringUtil.isBlank(startDate)) {
            pageParam.getPageData().put("startDate",startDate+" 00:00:00");
        }
        if (!StringUtil.isBlank(endDate)) {
            pageParam.getPageData().put("endDate",endDate+" 23:59:59");
        }

        List<JobExecutionExtend> records = baseMapper.selectList(pageParam);
        for (JobExecutionExtend jm : records) {
            String jobName = jm.getJobName();
            QueryWrapper<TaskInf> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_name", jobName);
            TaskInf one = taskInfService.getOne(queryWrapper);
            if (one != null) {
                jm.setAliasName(one.getAliasName());
                jm.setMachId(one.getMachId());
            }
        }
        return new PageResult<>(records, pageParam.getTotal());
    }
}
