package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hehanpeng
 */
@Data
public class RegisterTtoInputBean implements Serializable {
    private static final long serialVersionUID = -498173305468194357L;

    /**
     * <pre>
     * 流水号
     * 表字段 : tto_inf.REFERENCE
     * </pre>
     */
    private Long reference;

    /**
     * <pre>
     * 注册时间
     * 表字段 : tto_inf.REGISTER_TIME
     * </pre>
     */
    private Date registerTime;

    /**
     * <pre>
     * TYPE（全局/局部）
     * 0:全局 1：局部
     * 表字段 : tto_inf.TTO_TYPE
     * </pre>
     */
    private String ttoType;

    /**
     * <pre>
     * STATUS状态
     * 0：未执行
     * 1：已执行
     * 表字段 : tto_inf.TTO_STATUS
     * </pre>
     */
    private String ttoStatus;

    /**
     * <pre>
     * 业务类型
     * 表字段 : tto_inf.BIZ_TYPE
     * </pre>
     */
    private String bizType;

    /**
     * <pre>
     *
     * 表字段 : tto_inf.CALL_CLASS
     * </pre>
     */
    private String callClass;

    /**
     * <pre>
     * 回调方法
     * 表字段 : tto_inf.CALL_METHOD
     * </pre>
     */
    private String callMethod;

    /**
     * <pre>
     * 机器ID
     * 表字段 : tto_inf.WORK_ID
     * </pre>
     */
    private String workId;

    /**
     * <pre>
     * 超时时间
     * 表字段 : tto_inf.EXPIRED_TIME
     * </pre>
     */
    private String expiredTime;

    /**
     * <pre>
     * 机组ID
     * 表字段 : tto_inf.DATA_CENTER_ID
     * </pre>
     */
    private String dataCenterId;

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : tto_inf.EXT1
     * </pre>
     */
    private String ext1;

    /**
     * <pre>
     *
     * 表字段 : tto_inf.EXT2
     * </pre>
     */
    private String ext2;

    /**
     * <pre>
     *
     * 表字段 : tto_inf.EXT3
     * </pre>
     */
    private String ext3;

    /**
     * <pre>
     * 拓展字段1
     * 表字段 : tto_inf.EXT4
     * </pre>
     */
    private String ext4;

    /**
     * <pre>
     *
     * 表字段 : tto_inf.EXT5
     * </pre>
     */
    private String ext5;
}
