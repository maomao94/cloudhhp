package com.hehanpeng.framework.cloudhhp.module.core.event;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Component
public class DefaultEventManager implements EventManager {

    @Resource(name = "asyncServiceExecutor")
	Executor executor;

    private Map<Class, EventHandler> eventHandlers = new HashMap<Class, EventHandler>();

    private <T extends Event> void doService(final T event, final EventHandler<T> handler) throws InterruptedException {
        executor.execute(new Runnable() {
            public void run() {
                handler.handler(event);
            }
        });
    }

    public <T extends Event> void doService(final T event) {
        EventHandler<T> eventHandler;
        synchronized (eventHandlers) {
            eventHandler = (EventHandler<T>) eventHandlers.get(event.getClass());
        }
        try {
            doService(event, eventHandler);
        } catch (InterruptedException e) {
            throw new BizException(e);
        }

    }

    public void register(EventHandler<Event> handler) {
        synchronized (eventHandlers) {
            eventHandlers.put(handler.getAcceptedEventType(), handler);
        }
    }

    public void unregister(EventHandler<Event> handler) {
        synchronized (eventHandlers) {
            eventHandlers.remove(handler.getAcceptedEventType());
        }
    }

    public void setEventHandlers(List handlers) {
        for (Object handler : handlers) {
            register((EventHandler) handler);
        }
    }

    public void shutdown() {

    }
}
