package com.wf.ew.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.exception.ParameterException;
import com.wf.ew.common.message.MessageSender;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.model.JobExecutionExtend;
import com.wf.ew.system.model.StepExecution;
import com.wf.ew.system.service.JobExecutionService;
import com.wf.ew.system.service.StepExecutionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-25
 */
@Controller
@RequestMapping("/batch/jobExecution")
public class JobExecutionController {
    @Autowired
    private JobExecutionService jobExecutionService;
    @Autowired
    private StepExecutionService stepExecutionService;

    @Autowired
    private MessageSender sender;

    @RequiresPermissions("jobExecution:view")
    @RequestMapping()
    public String jobExecution(Model model) {
        return "batch/jobExecution.html";
    }


    /**
     * 查询任务详情所有数据
     **/
    @RequiresPermissions("jobExecution:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<JobExecutionExtend> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        PageResult<JobExecutionExtend> jobExecutionExtendPageResult = jobExecutionService.selectList(pageParam);
        return jobExecutionExtendPageResult;
    }

    /**
     * 查询step详情
     **/
    @RequiresPermissions("jobExecution:view")
    @ResponseBody
    @RequestMapping("/detail")
    public PageResult<StepExecution> detail(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        QueryWrapper<StepExecution> queryWrapper = new QueryWrapper<>();
        String excId = (String) pageParam.get("excId");
        if (StringUtil.isNotBlank(excId)) {
            queryWrapper.eq("JOB_EXECUTION_ID", excId);
        } else {
            throw new ParameterException("excId 不能为空");
        }
        return new PageResult<>(stepExecutionService.page(pageParam, queryWrapper).getRecords(), pageParam.getTotal());
    }

    @RequiresPermissions("jobExecution:operateTask")
    @ResponseBody
    @RequestMapping("/operateTask")
    public JsonResult operateTask(String jobExcId, String jobId, String machId, String type) {
        if (StringUtil.isBlank(jobExcId)) {
            return JsonResult.error("jobExcId 不能为空");
        }
        if (StringUtil.isBlank(jobId)) {
            return JsonResult.error("jobId 不能为空");
        }
        if (StringUtil.isBlank(machId)) {
            return JsonResult.error("machId 不能为空");
        }
        if (StringUtil.isBlank(type)) {
            return JsonResult.error("type 不能为空");
        }
        Map<String, String> message = new HashMap<>();
        message.put("jobId", jobId);
        message.put("jobExcId", jobExcId);
        message.put("type", type);
        sender.sendMessage("jobs.timed.task-" + machId, message);
        if ("restart".equals(type)) {
            return JsonResult.ok("重跑中,请刷新");
        } else {
            return JsonResult.ok("续跑中,请刷新");
        }
    }
}
