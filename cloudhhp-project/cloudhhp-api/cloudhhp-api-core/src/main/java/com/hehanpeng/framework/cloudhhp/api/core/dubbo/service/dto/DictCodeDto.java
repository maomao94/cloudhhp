package com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DictCodeDto implements Serializable {
    private static final long serialVersionUID = -6466965030299509705L;
    /**
     * 字典名称
     */
    private String dictCode;

    /**
     * 字典值
     */
    private String code;

    /**
     * 字典描述
     */
    private String name;

    /**
     * 字典分类描述
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
}