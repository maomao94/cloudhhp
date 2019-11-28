package com.wf.ew.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.system.model.LoginRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface LoginRecordService extends IService<LoginRecord> {

    // 分页查询
    PageResult<LoginRecord> listFull(PageParam page);

    // 不分页
    List<LoginRecord> listAll(Map pageData);

}
