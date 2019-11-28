package com.wf.ew.common.message;

public interface MessageSender {

	void sendMessage(String channel, Object content);
}
