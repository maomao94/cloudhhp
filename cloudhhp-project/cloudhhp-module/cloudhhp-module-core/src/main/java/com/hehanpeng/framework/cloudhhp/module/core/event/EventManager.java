package com.hehanpeng.framework.cloudhhp.module.core.event;

public interface EventManager {
    public <T extends Event> void doService(T event);
}
