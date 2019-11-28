package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hehanpeng
 */
@Data
public class CancelFwdInputBean implements Serializable {

    private static final long serialVersionUID = -5231861169911755297L;
    /**
     * <pre>
     * 自增序列主键
     * 表字段 : fwd_register.ID
     * </pre>
     */
    private Long id;

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
     * NEXT_ACTIVE_TIME下次激活时间
     * 表字段 : fwd_register.NEXT_ACTIVE_TIME
     * </pre>
     */
    
    private String nextActiveTime;
    
    
}
