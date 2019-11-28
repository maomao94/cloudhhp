package com.hehanpeng.framework.cloudhhp.module.gate.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.DictService;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.HelloDubboService;
import org.springframework.stereotype.Service;

/**
 * created with Intellij IDEA 2019.2
 *
 * @author: hehanpeng
 * Email: 287737281@qq.com
 * 2019/9/10 15:30
 */
@Service
public class RpcCommonService {

    @Reference(version = "${demo.service.version}", timeout = 10000, retries = 0, check = false)
    public HelloDubboService helloDubboService;

    @Reference(version = "${demo.service.version}", timeout = 10000, retries = 0, check = false)
    public DictService dictService;
}
