package com.hehanpeng.framework.cloudhhp.module.forward.domain.entity.forward;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "TTO_INF")
public class TtoInf {
    /**
     * 自增序列主键
     */
    @Id
    @Column(name = "TTOID")
    private Long ttoid;

    /**
     * 流水号
     */
    @Column(name = "REFERENCE")
    private Long reference;

    /**
     * 注册时间
     */
    @Column(name = "REGISTER_TIME")
    private Date registerTime;

    /**
     * 类型（全局/局部）0:全局 1：局部
     */
    @Column(name = "TTO_TYPE")
    private String ttoType;

    /**
     * STATUS状态 0：未执行 1：已执行
     */
    @Column(name = "TTO_STATUS")
    private String ttoStatus;

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
     * 机器ID
     */
    @Column(name = "WORK_ID")
    private String workId;

    /**
     * 执行时间
     */
    @Column(name = "EXCUTE_TIME")
    private Date excuteTime;

    @Column(name = "EXPIRED_TIME")
    private String expiredTime;

    /**
     * 机组ID
     */
    @Column(name = "DATA_CENTER_ID")
    private String dataCenterId;

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
}