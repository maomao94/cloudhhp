package com.wf.ew.system.model;

import lombok.Data;

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
@Data
public class JobExecutionExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobExecutionId;

    private Long version;

    private Long jobInstanceId;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    private String status;

    private String exitCode;

    private String exitMessage;

    private Date lastUpdated;

    private String jobConfigurationLocation;

    private String jobName;

    private String aliasName;

    private String machId;
}
