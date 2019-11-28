package com.wf.ew.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.system.model.JobExecution;
import com.wf.ew.system.model.JobExecutionExtend;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-25
 */
public interface JobExecutionService extends IService<JobExecution> {
    public PageResult<JobExecutionExtend> selectList(PageParam pageParam);
}
