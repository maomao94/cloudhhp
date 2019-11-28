package com.wf.ew.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.system.model.Authorities;
import com.wf.ew.system.service.AuthoritiesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 **/
@Controller
@RequestMapping("/system/authorities")
public class AuthoritiesController extends BaseController {
    @Autowired
    private AuthoritiesService authoritiesService;

    @RequiresPermissions("authorities:view")
    @RequestMapping()
    public String authorities(Model model) {
        List<Authorities> authorities = authoritiesService.list(new QueryWrapper<Authorities>().eq("is_menu", 0).orderByAsc("order_number"));
        model.addAttribute("authorities", authorities);
        return "system/authorities.html";
    }

    /**
     * 查询所有权限
     **/
    @RequiresPermissions("authorities:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Authorities> list() {
        List<Authorities> authorities = authoritiesService.list(new QueryWrapper<Authorities>().orderByAsc("order_number"));
        return new PageResult<>(authorities);
    }

    /**
     * 添加权限
     */
    @RequiresPermissions("authorities:update")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Authorities authorities) {
        if (authoritiesService.save(authorities)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改权限
     */
    @RequiresPermissions("authorities:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Authorities authorities) {
        if (authoritiesService.updateById(authorities)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除权限
     */
    @RequiresPermissions("authorities:update")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(Integer authorityId) {
        if (authoritiesService.removeById(authorityId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
