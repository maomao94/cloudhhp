package com.wf.ew.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.system.dao.TaskInfMapper;
import com.wf.ew.system.model.TaskInf;
import com.wf.ew.system.service.TaskInfService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务控制表 服务实现类
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-24
 */
@Service
public class TaskInfServiceImpl extends ServiceImpl<TaskInfMapper, TaskInf> implements TaskInfService {

}
