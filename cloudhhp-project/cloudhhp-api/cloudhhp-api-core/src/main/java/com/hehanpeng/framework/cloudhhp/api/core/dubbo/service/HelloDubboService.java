package com.hehanpeng.framework.cloudhhp.api.core.dubbo.service;

import java.util.HashMap;

public interface HelloDubboService {
    String hello(String name);

    void testFwd(HashMap<String, Object> map);
}
