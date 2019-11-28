package com.hehanpeng.framework.cloudhhp.module.core.constant;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description: 系统返回码定义
 * @date 2017-07-05
 */
public enum CoreErrEnum {

    /*
    0001|系统错误 |系统超时或异常|系统异常，请用相同参数重新调用
    0002|参数错误
    0003|DB错误
    0004|流水号重复
    */

    ERR_0001("0001", "系统错误"),
    ERR_0002("0002", "参数错误"),
    ERR_0003("0003", "DB错误"),
    ERR_0004("0004", "记录不存在"),
    ERR_0005("0005", "Dubbo调用异常"),
    ERR_0006("0006", "锁未释放");
    private String code;
    private String message;

    CoreErrEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
