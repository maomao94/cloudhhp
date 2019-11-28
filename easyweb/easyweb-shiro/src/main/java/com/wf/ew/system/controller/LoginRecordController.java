package com.wf.ew.system.controller;

import com.wf.ew.common.PageParam;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.PoiUtil;
import com.wf.ew.system.model.LoginRecord;
import com.wf.ew.system.service.LoginRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录日志管理
 **/
@Controller
@RequestMapping("/system/loginRecord")
public class LoginRecordController {
    @Autowired
    private LoginRecordService loginRecordService;

    @RequiresPermissions("loginRecord:view")
    @RequestMapping()
    public String loginRecord() {
        return "system/loginRecord.html";
    }

    /**
     * 查询所有
     **/
    @RequiresPermissions("loginRecord:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<LoginRecord> list(HttpServletRequest request) {
        return loginRecordService.listFull(new PageParam(request).setDefaultOrder(null, new String[]{"create_time"}));
    }

    /**
     * 导出数据
     **/
    @RequiresPermissions("loginRecord:view")
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String[]> exportList = new ArrayList<>();
        String[] titles = new String[]{"账号", "用户名", "IP", "设备", "设备类型", "浏览器"};
        exportList.add(titles);
        List<LoginRecord> list = loginRecordService.listAll(new PageParam(request).getPageData());
        for (LoginRecord one : list) {
            String[] strs = new String[]{one.getUsername(), one.getNickName(), one.getIpAddress(), one.getDevice(), one.getOsName(), one.getBrowserType()};
            exportList.add(strs);
        }
        PoiUtil.exportData(exportList, "登录日志", response);
    }

}
