package com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DictDto implements Serializable {
    private static final long serialVersionUID = -3542482808305931014L;
    /**
     * 字典分类代码
     */
    private String dictCode;

    /**
     * 字典分类名称
     */
    private String dictName;

    /**
     * 字典分类描述
     */
    private String dictRemark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    private List<DictCodeDto> dictCodeDtoList;
}