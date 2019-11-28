package com.hehanpeng.framework.cloudhhp.module.batch.dao.batch.extend;

import com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch.BatchJobExecutionParams;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BatchJobExecutionParamsExtendMapper extends Mapper<BatchJobExecutionParams> {
    List<BatchJobExecutionParams> selectByJobId(Map<String, Object> map);
}