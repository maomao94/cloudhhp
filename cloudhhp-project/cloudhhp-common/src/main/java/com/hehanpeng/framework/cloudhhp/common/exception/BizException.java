package com.hehanpeng.framework.cloudhhp.common.exception;

public class BizException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6854179778617753296L;

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

    public BizException() {
        super();
    }

    public BizException(String errorCode, String errorMsg, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(String errorMsg) {
        super(errorMsg);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
