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
@Table(name = "DICT")
public class Dict {
    /**
     * 字典分类代码
     */
    @Id
    @Column(name = "DICT_CODE")
    private String dictCode;

    /**
     * 字典分类名称
     */
    @Column(name = "DICT_NAME")
    private String dictName;

    /**
     * 字典分类描述
     */
    @Column(name = "DICT_REMARK")
    private String dictRemark;

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