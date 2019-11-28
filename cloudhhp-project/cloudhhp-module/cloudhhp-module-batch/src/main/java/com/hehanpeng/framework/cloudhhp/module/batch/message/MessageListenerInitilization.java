package com.hehanpeng.framework.cloudhhp.module.batch.message;

import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageHandler;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageQueueReceiver;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageListenerInitilization implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {
	
	private MessageQueueReceiver queue;
	
	private static Map<String, MessageListener> listeners = new ConcurrentHashMap<String, MessageListener>();
	
	private ApplicationContext context;

	public void setQueue(MessageQueueReceiver queue) {
		this.queue = queue;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public void stopMessageHandler(String channel) {
		MessageListener listener = listeners.get(channel);
		listener.close();
	}
	
	public void startMessageHandler(String channel) {
		MessageListener listener = listeners.get(channel);
		Thread thread = new Thread(listener);
		thread.start();
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		Map<String, MessageHandler> handlers = context.getBeansOfType(MessageHandler.class);
		for(String key : handlers.keySet()) {
			MessageHandler mh = handlers.get(key);
			MessageListener listener = new MessageListener(mh, queue);
			Thread thread = new Thread(listener);
			thread.start();
			listeners.put(mh.getChannelName(), listener);
		}
	}
}
