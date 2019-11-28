package com.wf.ew.common.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable{
	private static final long serialVersionUID = -1066142971103437946L;

	private String msgid;
	
	private Object content;

	public Message() {
		
	}
	public Message(Object content) {
		this.content = content;
	}
}
