package com.wf.ew.system.model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author hehanpeng
 * @since 2019-10-31
 */
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictRemark() {
        return dictRemark;
    }

    public void setDictRemark(String dictRemark) {
        this.dictRemark = dictRemark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "Dict{" +
        ", dictCode=" + dictCode +
        ", dictName=" + dictName +
        ", dictRemark=" + dictRemark +
        ", createTime=" + createTime +
        ", lastUpdateTime=" + lastUpdateTime +
        "}";
    }
}
