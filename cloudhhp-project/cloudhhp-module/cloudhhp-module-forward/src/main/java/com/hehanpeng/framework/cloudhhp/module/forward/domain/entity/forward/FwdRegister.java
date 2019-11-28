package com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "FWD_REGISTER")
public class FwdRegister {
    /**
     * 自增序列主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 注册时间
     */
    @Column(name = "REGISTER_TIME")
    private Date registerTime;

    /**
     * 流水号
     */
    @Column(name = "REFERENCE")
    private Long reference;

    /**
     * 机器ID
     */
    @Column(name = "WORK_ID")
    private String workId;

    /**
     * 下次激活时间
     */
    @Column(name = "NEXT_ACTIVE_TIME")
    private Date nextActiveTime;

    /**
     * 类型（全局/局部）0:全局 1：局部
     */
    @Column(name = "FWD_TYPE")
    private String fwdType;

    /**
     * 重试次数
     */
    @Column(name = "RETRY_COUNT")
    private Integer retryCount;

    /**
     * 重试次数上限
     */
    @Column(name = "RETRY_LIMIT")
    private Integer retryLimit;

    /**
     * 基本时间间隔，单位毫秒
     */
    @Column(name = "BASE_INTERVAL")
    private Long baseInterval;

    /**
     * 间隔时间增量，单位毫秒
     */
    @Column(name = "INTERVAL_DELTA")
    private Long intervalDelta;

    /**
     * 状态 等待W，转发中D，注销C，转发次数达上限U
     */
    @Column(name = "FWD_STATUS")
    private String fwdStatus;

    /**
     * 业务类型
     */
    @Column(name = "BIZ_TYPE")
    private String bizType;

    /**
     * 回调CLASS
     */
    @Column(name = "CALL_CLASS")
    private String callClass;

    /**
     * 回调方法
     */
    @Column(name = "CALL_METHOD")
    private String callMethod;

    /**
     * 机组ID
     */
    @Column(name = "DATA_CENTER_ID")
    private String dataCenterId;

    /**
     * 记录创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    /**
     * 备用1
     */
    @Column(name = "EXT1")
    private String ext1;

    /**
     * 备用2
     */
    @Column(name = "EXT2")
    private String ext2;

    /**
     * 备用3
     */
    @Column(name = "EXT3")
    private String ext3;

    /**
     * 备用4
     */
    @Column(name = "EXT4")
    private String ext4;

    /**
     * 备用5
     */
    @Column(name = "EXT5")
    private String ext5;
}