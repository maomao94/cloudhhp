package com.wf.ew.system.model;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hehanpeng
 * @since 2019-08-25
 */
@TableName("BATCH_STEP_EXECUTION")
public class StepExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long stepExecutionId;

    private Long version;

    private String stepName;

    private Long jobExecutionId;

    private Date startTime;

    private Date endTime;

    private String status;

    private Long commitCount;

    private Long readCount;

    private Long filterCount;

    private Long writeCount;

    private Long readSkipCount;

    private Long writeSkipCount;

    private Long processSkipCount;

    private Long rollbackCount;

    private String exitCode;

    private String exitMessage;

    private Date lastUpdated;

    public Long getStepExecutionId() {
        return stepExecutionId;
    }

    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(Long commitCount) {
        this.commitCount = commitCount;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    public Long getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Long filterCount) {
        this.filterCount = filterCount;
    }

    public Long getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(Long writeCount) {
        this.writeCount = writeCount;
    }

    public Long getReadSkipCount() {
        return readSkipCount;
    }

    public void setReadSkipCount(Long readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    public Long getWriteSkipCount() {
        return writeSkipCount;
    }

    public void setWriteSkipCount(Long writeSkipCount) {
        this.writeSkipCount = writeSkipCount;
    }

    public Long getProcessSkipCount() {
        return processSkipCount;
    }

    public void setProcessSkipCount(Long processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    public Long getRollbackCount() {
        return rollbackCount;
    }

    public void setRollbackCount(Long rollbackCount) {
        this.rollbackCount = rollbackCount;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "StepExecution{" +
        ", stepExecutionId=" + stepExecutionId +
        ", version=" + version +
        ", stepName=" + stepName +
        ", jobExecutionId=" + jobExecutionId +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", status=" + status +
        ", commitCount=" + commitCount +
        ", readCount=" + readCount +
        ", filterCount=" + filterCount +
        ", writeCount=" + writeCount +
        ", readSkipCount=" + readSkipCount +
        ", writeSkipCount=" + writeSkipCount +
        ", processSkipCount=" + processSkipCount +
        ", rollbackCount=" + rollbackCount +
        ", exitCode=" + exitCode +
        ", exitMessage=" + exitMessage +
        ", lastUpdated=" + lastUpdated +
        "}";
    }
}
