package com.wf.ew.system.service;

import com.wf.ew.system.model.RoleAuthorities;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface RoleAuthoritiesService extends IService<RoleAuthorities> {
    boolean updateRoleAuth(Integer roleId, List<Integer> authIds);
}
