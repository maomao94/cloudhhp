package com.wf.ew.system.model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 数据字典子节点
 * </p>
 *
 * @author hehanpeng
 * @since 2019-10-31
 */
public class DictCode implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "DictCode{" +
        ", dictCode=" + dictCode +
        ", code=" + code +
        ", name=" + name +
        ", remark=" + remark +
        ", createTime=" + createTime +
        ", lastUpdateTime=" + lastUpdateTime +
        "}";
    }
}
