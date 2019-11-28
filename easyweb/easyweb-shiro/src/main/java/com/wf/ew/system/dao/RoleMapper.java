package com.wf.ew.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wf.ew.system.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> listByUserId(@Param("userId") Integer userId);

    Role getByUserId(@Param("userId") Integer userId);
}
