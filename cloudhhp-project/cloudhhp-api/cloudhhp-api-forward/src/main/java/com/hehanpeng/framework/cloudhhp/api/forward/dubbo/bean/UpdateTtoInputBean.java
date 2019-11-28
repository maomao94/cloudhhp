package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hehanpeng
 */
@Data
public class UpdateTtoInputBean implements Serializable {
    private static final long serialVersionUID = 526997333475775096L;

    /**
     * <pre>
     * 自增序列主键
     * 表字段 : tto_inf.TTOID
     * </pre>
     */
    private Long ttoid;

    /**
     * <pre>
     * 超时时间
     * 表字段 : tto_inf.EXPIRED_TIME
     * </pre>
     */
    private String expiredTime;

    /**
     * 上游系统时间
     */
    private Date uppSysTime;
    
    /**
     * 重试次数
     */
    private int retryCount;
    
    /**
     * 下次激活时间
     */
    private Date nextActiveTime;
}
