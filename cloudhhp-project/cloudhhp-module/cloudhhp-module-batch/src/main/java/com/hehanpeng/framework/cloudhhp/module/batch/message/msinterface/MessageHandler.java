package com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface;

import com.hehanpeng.framework.cloudhhp.module.batch.message.Message;

public interface MessageHandler {
	
	public String getChannelName();
	
	void processMessage(Message message);
}
