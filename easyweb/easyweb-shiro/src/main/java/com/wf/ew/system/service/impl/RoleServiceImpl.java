package com.wf.ew.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.system.dao.RoleMapper;
import com.wf.ew.system.model.Role;
import com.wf.ew.system.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> listByUserId(Integer userId) {
        return baseMapper.listByUserId(userId);
    }

    @Override
    public Role getByUserId(Integer userId) {
        return baseMapper.getByUserId(userId);
    }
}
