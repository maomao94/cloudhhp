package com.wf.ew.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.system.model.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface RoleService extends IService<Role> {

    List<Role> listByUserId(Integer userId);

    Role getByUserId(Integer userId);
}
