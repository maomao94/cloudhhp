package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hehanpeng
 */
@Data
public class UpdateFwdInputBean implements Serializable {

	private static final long serialVersionUID = -8378365018996888760L;

	/**
     * <pre>
     * 自增序列主键
     * 表字段 : fwd_register.ID
     * </pre>
     */
    private Long id;
    
    /**
     * 重试次数上限
     */
    private int retryLimit;
    
    /**
     * 下次激活时间
     */
    private Date nextActiveTime;
}
