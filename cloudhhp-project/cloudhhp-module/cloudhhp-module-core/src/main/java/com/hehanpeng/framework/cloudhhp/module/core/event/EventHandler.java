package com.hehanpeng.framework.cloudhhp.module.core.event;

public interface EventHandler<T extends Event> {

    public void handler(T event);

    public Class<T> getAcceptedEventType();
}
