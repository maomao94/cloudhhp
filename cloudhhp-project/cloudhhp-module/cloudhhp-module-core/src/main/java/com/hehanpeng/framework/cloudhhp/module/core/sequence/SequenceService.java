package com.hehanpeng.framework.cloudhhp.module.core.sequence;

public interface SequenceService {
    /**
     * 生成序列号
     * @param seqType 序列号类型
     * @return 序列号
     */
    public Long nextId(String seqType);
}
