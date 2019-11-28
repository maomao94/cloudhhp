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
@Table(name = "FWD_DETAIL")
public class FwdDetail {
    /**
     * 自增主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 所属的转发登记记录ID
     */
    @Column(name = "REGID")
    private Long regid;

    /**
     * 转发时间
     */
    @Column(name = "FORWARD_TIME")
    private Date forwardTime;

    /**
     * 调用失败F 成功S
     */
    @Column(name = "FORWARD_FLAG")
    private String forwardFlag;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;
}