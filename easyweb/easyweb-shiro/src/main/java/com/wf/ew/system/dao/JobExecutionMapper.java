package com.wf.ew.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wf.ew.common.PageParam;
import com.wf.ew.system.model.JobExecution;
import com.wf.ew.system.model.JobExecutionExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-25
 */
public interface JobExecutionMapper extends BaseMapper<JobExecution> {
    List<JobExecutionExtend> selectList(@Param("page") PageParam page);
}