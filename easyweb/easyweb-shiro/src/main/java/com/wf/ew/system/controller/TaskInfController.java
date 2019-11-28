package com.wf.ew.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.constants.BatchConstants;
import com.wf.ew.common.message.MessageSender;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.model.TaskInf;
import com.wf.ew.system.service.TaskInfService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 定时任务控制表 前端控制器
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-24
 */
@Controller
@RequestMapping("/batch/taskInf")
public class TaskInfController extends BaseController {
    @Autowired
    private TaskInfService taskInfService;

    @Autowired
    private MessageSender sender;

    @RequiresPermissions("taskInf:view")
    @RequestMapping()
    public String taskInfs(Model model) {
        return "batch/taskInf.html";
    }


    /**
     * 查询定时任务控制表所有数据
     **/
    @RequiresPermissions("taskInf:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<TaskInf> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        QueryWrapper<TaskInf> queryWrapper = new QueryWrapper<>();
        String keywords = (String) pageParam.get("keywords");
        if (StringUtil.isNotBlank(keywords)) {
            queryWrapper.like("task_Name", keywords).or().like("alias_Name", keywords);
        }
        return new PageResult<>(taskInfService.page(pageParam, queryWrapper).getRecords(), pageParam.getTotal());
    }

    /**
     * 添加任务
     **/
    @RequiresPermissions("taskInf:update")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(TaskInf taskInf) {
        taskInf.setCreateTime(new Date());
        taskInf.setTaskStat(BatchConstants.TASK_STOP_STATUS);
        taskInf.setOprId(String.valueOf(getLoginUserId()));
        if (taskInfService.save(taskInf)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改任务
     **/
    @RequiresPermissions("taskInf:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(TaskInf taskInf) {
        taskInf.setUpdateTime(new Date());
        taskInf.setOprId(String.valueOf(getLoginUserId()));
        if (taskInfService.updateById(taskInf)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除角色
     **/
    @RequiresPermissions("taskInf:update")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(Long taskInfId) {
        if (taskInfService.removeById(taskInfId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改任务状态
     **/
    @RequiresPermissions("taskInf:update")
    @ResponseBody
    @RequestMapping("/updateState")
    public JsonResult updateState(Long taskInfoId, String taskStat) {
        if (taskInfoId == null) {
            return JsonResult.error("参数taskInfoId不能为空");
        }
        if (taskStat == null) {
            return JsonResult.error("参数taskStat不能空");
        }
        TaskInf taskInf = taskInfService.getById(taskInfoId);
        if (taskInf != null) {
            //发送redis消息给batch系统，启动定时任务
            Map<String, String> message = new HashMap<>();
            message.put("jobName", taskInf.getTaskName());
            message.put("expression", taskInf.getTaskExpress());
            message.put("status", taskStat);
            sender.sendMessage("trigger.timed.task-" + taskInf.getMachId(), message);
            return JsonResult.ok("发送中[异步]");
        } else {
            return JsonResult.error("任务不存在");
        }
    }
}
