package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hehanpeng
 */
@Data
public class CancelTtoInputBean implements Serializable {
    private static final long serialVersionUID = -6386051129861525390L;

    /**
     * <pre>
     * 自增序列主键
     * 表字段 : tto_inf.TTOID
     * </pre>
     */
    private Long ttoid;

    /**
     * <pre>
     * STATUS状态
     * 0：未执行
     * 1：已执行
     * 表字段 : tto_inf.TTO_STATUS
     * </pre>
     */
    private String ttoStatus;
}
