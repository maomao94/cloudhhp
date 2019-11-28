package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean;

import com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base.BaseOutputBean;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hehanpeng
 */
@Data
@ToString(callSuper = true)
public class RegisterTtoOutputBean extends BaseOutputBean implements Serializable {
    private static final long serialVersionUID = -6917520927326191284L;

    /**
     * <pre>
     * 自增序列主键
     * 表字段 : tto_inf.TTOID
     * </pre>
     */
    private Long ttoid;
}
