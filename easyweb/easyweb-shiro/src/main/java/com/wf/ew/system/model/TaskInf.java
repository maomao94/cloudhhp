package com.wf.ew.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定时任务控制表
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-24
 */
@TableName("BATCH_TASK_INF")
public class TaskInf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 定时任务表达式
     */
    private String taskExpress;

    /**
     * 任务别名
     */
    private String aliasName;

    /**
     * 操作员id
     */
    private String oprId;

    /**
     * 状态 00停止  01启动
     */
    private String taskStat;

    /**
     * 任务机器号
     */
    private String machId;

    /**
     * 所属集群
     */
    private String clusterName;

    /**
     * 启动任务参数
     */
    private String taskPara;

    private Date updateTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskExpress() {
        return taskExpress;
    }

    public void setTaskExpress(String taskExpress) {
        this.taskExpress = taskExpress;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getTaskStat() {
        return taskStat;
    }

    public void setTaskStat(String taskStat) {
        this.taskStat = taskStat;
    }

    public String getMachId() {
        return machId;
    }

    public void setMachId(String machId) {
        this.machId = machId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getTaskPara() {
        return taskPara;
    }

    public void setTaskPara(String taskPara) {
        this.taskPara = taskPara;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TaskInf{" +
        ", id=" + id +
        ", taskName=" + taskName +
        ", taskExpress=" + taskExpress +
        ", aliasName=" + aliasName +
        ", oprId=" + oprId +
        ", taskStat=" + taskStat +
        ", machId=" + machId +
        ", clusterName=" + clusterName +
        ", taskPara=" + taskPara +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        "}";
    }
}
