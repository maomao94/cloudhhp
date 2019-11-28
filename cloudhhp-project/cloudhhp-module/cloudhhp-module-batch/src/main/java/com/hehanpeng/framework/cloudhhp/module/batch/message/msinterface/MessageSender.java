package com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface;

public interface MessageSender {

	void sendMessage(String channel, Object content);
}
