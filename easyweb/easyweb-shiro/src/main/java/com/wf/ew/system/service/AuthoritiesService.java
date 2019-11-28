package com.wf.ew.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.system.model.Authorities;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface AuthoritiesService extends IService<Authorities> {

    List<Authorities> listByUserId(Integer userId);

    List<Authorities> listByRoleIds(List<Integer> roleIds);

    List<Authorities> listByRoleId(Integer roleId);
}
