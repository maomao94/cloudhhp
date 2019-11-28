package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.constant;

/**
 * @author hehanpeng
 */
public class ForwardConstant {

    /**
     * 随机通讯码不重复的时间间隔(ms)
     */
    public static final long RPC_SEQ_NO_NOT_REPEAT_INTERVAL = 5 * 1000;
    
    /**
     * 全局超时
     */
    public static final String TTO_TYPE_GLOBAL = "0";
    
    /**
     * 局部超时
     */
    public static final String TTO_TYPE_PART = "1";

    /**
     * 未执行
     */
    public static final String TTO_STATUS_UNEXECUTED = "0";
    
    /**
     * 已执行
     */
    public static final String TTO_STATUS_EXECUTED = "1";

    /**
     * 返回成功响应码
     */
    public static final String RETURN_CODE_SUCCESS = "0000";

    /**
     * 返回成功响应信息
     */
    public static final String RETURN_MSG_SUCCESS = "SUCCESS";
    
}
