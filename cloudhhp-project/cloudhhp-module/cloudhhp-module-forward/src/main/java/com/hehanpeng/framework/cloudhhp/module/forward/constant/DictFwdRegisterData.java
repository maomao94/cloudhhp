package com.hehanpeng.framework.cloudhhp.module.forward.constant;


/**
 * @author hehanpeng
 * 2018/9/17 17:45
 */
public class DictFwdRegisterData {

    /**
     * <pre>
     * 自增序列主键
     * 表字段 : fwd_register.ID
     * </pre>
     */
    public static final String ID = "id";
    /**
     * <pre>
     * 流水号
     * 表字段 : fwd_register.REFERENCE
     * </pre>
     */
    public static final String REFERENCE = "reference";

    /**
     * <pre>
     * 注册时间
     * 表字段 : fwd_register.REGISTER_TIME
     * </pre>
     */
    public static final String REGISTERTIME = "registerTime";

    /**
     * <pre>
     * STATUS状态
     * 等待W，转发D，完成S，失败F，撤销C
     * 表字段 : fwd_register.FWD_STATUS
     * </pre>
     */
    public static final String FWDSTATUS = "fwdStatus";

    /**
     * <pre>
     * 业务类型
     * 表字段 : fwd_register.BIZ_TYPE
     * </pre>
     */
    public static final String BIZTYPE = "bizType";

    /**
     * <pre>
     *
     * 表字段 : fwd_register.CALL_CLASS
     * </pre>
     */
    public static final String CALLCLASS = "callClass";

    /**
     * <pre>
     * 回调方法
     * 表字段 : fwd_register.CALL_METHOD
     * </pre>
     */
    public static final String CALLMETHOD = "callMethod";

    /**
     * <pre>
     * 机器ID
     * 表字段 : fwd_register.WORK_ID
     * </pre>
     */
    public static final String WORKID = "workId";

    /**
     * <pre>
     * 机组ID
     * 表字段 : fwd_register.DATA_CENTER_ID
     * </pre>
     */
    public static final String DATACENTERID = "dataCenterId";

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : fwd_register.EXT1
     * </pre>
     */
    public static final String EXT1 = "ext1";

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT2
     * </pre>
     */
    public static final String EXT2 = "ext2";

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT3
     * </pre>
     */
    public static final String EXT3 = "ext3";

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : fwd_register.EXT4
     * </pre>
     */
    public static final String EXT4 = "ext4";

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT5
     * </pre>
     */
    public static final String EXT5 = "ext5";

    /**
     * <pre>
     * 上游系统时间
     * 新增字段 : uppsystime
     * </pre>
     */
    public static final String UPPSYSTIME = "uppsystime";
    
    
    
    /**
     * <pre>
     * 重试次数
     * 表字段 : fwd_register.RETRY_COUNT
     * </pre>
     */
    public static final String RETRYCOUNT="retryCount";
    
    /**
     * <pre>
     * 重试次数上限
     * 表字段 : fwd_register.RETRY_LIMIT
     * </pre>
     */
    public static final String RETRYLIMIT="retryLimit";
    
    /**
     * <pre>
     * 基本时间间隔
     * 表字段 : fwd_register.BASE_INTERVAL
     * </pre>
     */
    public static final String BASEINTERVAL="baseInterval";
    
    /**
     * <pre>
     * 间隔时间增量
     * 表字段 : fwd_register.INTERVAL_DELTA
     * </pre>
     */
    public static final String INTERVALDELTA="intervalDelta";
    
    /**
     * <pre>
     * 转发类型 0超时转发，1存储转发
     * 表字段 : fwd_register.FWD_TYPE
     * </pre>
     */
    public static final String FWDTYPE="fwdType";
    /**
     * <pre>
     *  下次激活时间
     * 表字段 : fwd_register.NEXT_ACTIVE_TIME
     * </pre>
     */
    public static final String NEXTACTIVETIME="nextActiveTime";
    

    
    
}
