package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hehanpeng
 */
@Data
public class RegisterFwdInputBean implements Serializable {
	private static final long serialVersionUID = -3145372965841510734L;
	
	/**
     * <pre>
     * 自增主键
     * 表字段 : fwd_register.ID
     * </pre>
     */
	 private Long id;
	/**
     * <pre>
     * 
     * 表字段 : fwd_register.REFERENCE
     * </pre>
     */
    private Long reference;

    /**
     * <pre>
     * 注册时间
     * 表字段 : fwd_register.REGISTER_TIME
     * </pre>
     */
    private Date registerTime;

    /**
     * <pre>
     * TYPE（全局/局部）
     * 0:全局 1：局部
     * 表字段 : fwd_register.FWD_TYPE
     * </pre>
     */
    private String fwdType;

    /**
     * <pre>
     * STATUS状态
     * 0：未执行
     * 1：已执行
     * 表字段 : fwd_register.FWD_STATUS
     * </pre>
     */
    private String fwdStatus;

    /**
     * <pre>
     * 业务类型
     * 表字段 : fwd_register.BIZ_TYPE
     * </pre>
     */
    private String bizType;

    /**
     * <pre>
     *
     * 表字段 : fwd_register.CALL_CLASS
     * </pre>
     */
    private String callClass;

    /**
     * <pre>
     * 回调方法
     * 表字段 : fwd_register.CALL_METHOD
     * </pre>
     */
    private String callMethod;

    /**
     * <pre>
     * 机器ID
     * 表字段 : fwd_register.WORK_ID
     * </pre>
     */
    private String workId;

    /**
     * <pre>
     * 机组ID
     * 表字段 : fwd_register.DATA_CENTER_ID
     * </pre>
     */
    private String dataCenterId;

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : fwd_register.EXT1
     * </pre>
     */
    private String ext1;

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT2
     * </pre>
     */
    private String ext2;

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT3
     * </pre>
     */
    private String ext3;

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : fwd_register.EXT4
     * </pre>
     */
    private String ext4;

    /**
     * <pre>
     *
     * 表字段 : fwd_register.EXT5
     * </pre>
     */
    private String ext5;
    
    /**
     * <pre>
     *
     * 表字段 : fwd_register.RETRY_COUNT
     * </pre>
     */
    private Integer retryCount;
    
    /**
     * <pre>
     *
     * 表字段 : fwd_register.RETRY_LIMIT
     * </pre>
     */
    private Integer retryLimit;
    
    /**
     * <pre>
     *
     * 表字段 : fwd_register.BASE_INTERVAL
     * </pre>
     */
    private Long baseInterval;
    
    /**
     * <pre>
     *
     * 表字段 : fwd_register.INTERVAL_DELTA
     * </pre>
     */
    private Long intervalDelta;
    
    /**
     * <pre>
     *
     * 表字段 : fwd_register.NEXT_ACTIVE_TIME
     * </pre>
     */
    private Date nextActiveTime;
    
}
