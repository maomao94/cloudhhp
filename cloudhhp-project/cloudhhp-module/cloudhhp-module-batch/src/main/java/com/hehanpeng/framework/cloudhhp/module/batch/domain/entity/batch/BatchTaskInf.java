package com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch;

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
@Table(name = "BATCH_TASK_INF")
public class BatchTaskInf {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 任务名
     */
    @Column(name = "TASK_NAME")
    private String taskName;

    /**
     * 定时任务表达式
     */
    @Column(name = "TASK_EXPRESS")
    private String taskExpress;

    /**
     * 任务别名
     */
    @Column(name = "ALIAS_NAME")
    private String aliasName;

    /**
     * 操作员id
     */
    @Column(name = "OPR_ID")
    private String oprId;

    /**
     * 状态 00停止  01启动
     */
    @Column(name = "TASK_STAT")
    private String taskStat;

    /**
     * 任务机器号
     */
    @Column(name = "MACH_ID")
    private String machId;

    /**
     * 所属集群
     */
    @Column(name = "CLUSTER_NAME")
    private String clusterName;

    /**
     * 启动任务参数
     */
    @Column(name = "TASK_PARA")
    private String taskPara;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "CREATE_TIME")
    private Date createTime;
}