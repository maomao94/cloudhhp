package com.hehanpeng.framework.cloudhhp.module.core.event.handler;

import com.hehanpeng.framework.cloudhhp.module.core.annotation.ExecutorHandler;
import com.hehanpeng.framework.cloudhhp.module.core.event.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExecutorHandler
public class TestEventHandler implements EventHandler<TestEvent> {
    @Override
    public void handler(TestEvent event) {
        log.info("TestEventHandler start");
        //todo
        log.info("TestEventHandler end");
    }

    @Override
    public Class<TestEvent> getAcceptedEventType() {
        return TestEvent.class;
    }
}
