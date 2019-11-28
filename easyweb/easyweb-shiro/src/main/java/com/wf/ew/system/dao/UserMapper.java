package com.wf.ew.system.dao;

import com.wf.ew.common.PageParam;
import com.wf.ew.system.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);

    List<User> listFull(@Param("page") PageParam page);
}
