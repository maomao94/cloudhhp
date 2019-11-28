package com.hehanpeng.framework.cloudhhp.module.batch.domain.entity.batch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams {
    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

    @Column(name = "TYPE_CD")
    private String typeCd;

    @Column(name = "KEY_NAME")
    private String keyName;

    @Column(name = "STRING_VAL")
    private String stringVal;

    @Column(name = "DATE_VAL")
    private Date dateVal;

    @Column(name = "LONG_VAL")
    private Long longVal;

    @Column(name = "DOUBLE_VAL")
    private Double doubleVal;

    @Column(name = "IDENTIFYING")
    private String identifying;
}