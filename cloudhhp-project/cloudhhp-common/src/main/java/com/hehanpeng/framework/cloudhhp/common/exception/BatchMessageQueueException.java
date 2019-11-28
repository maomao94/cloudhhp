package com.hehanpeng.framework.cloudhhp.common.exception;

public class BatchMessageQueueException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6377097450451879217L;

	public BatchMessageQueueException(String message) {
		super(message);
	}
	
	public BatchMessageQueueException(Throwable cause) {
		super(cause);
	}
	
	public BatchMessageQueueException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public String getMsg() {
		return this.getMessage();
	}
}
