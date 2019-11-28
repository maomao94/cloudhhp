package com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core;

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
@Table(name = "CORE_FLOW_CONTEXT")
public class CoreFlowContext {
    /**
     * 交易上下文自增主键
     */
    @Id
    @Column(name = "ID_FLOW_CONTEXT")
    private Long idFlowContext;

    /**
     * 工作流名称
     */
    @Column(name = "WORKFLOW_NAME")
    private String workflowName;

    @Column(name = "FLOW_STATUS")
    private String flowStatus;

    /**
     * 交易请求时间
     */
    @Column(name = "REQ_TIME")
    private Date reqTime;

    /**
     * 交易响应时间
     */
    @Column(name = "RSP_TIME")
    private Date rspTime;

    /**
     * 当前工作状态
     */
    @Column(name = "CURRENT_WORK")
    private String currentWork;

    /**
     * 处理结果
     */
    @Column(name = "PROCESS_RESULT")
    private String processResult;
}