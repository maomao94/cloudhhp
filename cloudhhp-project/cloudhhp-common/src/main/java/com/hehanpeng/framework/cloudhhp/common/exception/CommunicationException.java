package com.hehanpeng.framework.cloudhhp.common.exception;

/**
 * created with Intellij IDEA 2017.2
 *
 * @author: hehanpeng
 * Email: 287737281@qq.com
 * Date: 2019/6/3
 * Time: 10:18
 */
public class CommunicationException extends RuntimeException {
    private static final long serialVersionUID = -2358199091417802759L;
    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public CommunicationException() {
        super();
    }

    public CommunicationException(String errorCode, String errorMsg, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommunicationException(String errorMsg) {
        super(errorMsg);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
