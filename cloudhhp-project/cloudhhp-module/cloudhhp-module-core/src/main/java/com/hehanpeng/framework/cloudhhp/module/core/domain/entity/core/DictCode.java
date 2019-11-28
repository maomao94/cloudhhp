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
@Table(name = "DICT_CODE")
public class DictCode {
    /**
     * 字典名称
     */
    @Id
    @Column(name = "DICT_CODE")
    private String dictCode;

    /**
     * 字典值
     */
    @Id
    @Column(name = "CODE")
    private String code;

    /**
     * 字典描述
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 字典分类描述
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;
}