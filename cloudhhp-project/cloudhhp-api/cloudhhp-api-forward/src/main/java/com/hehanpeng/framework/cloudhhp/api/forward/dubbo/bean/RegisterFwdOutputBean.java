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
public class RegisterFwdOutputBean extends BaseOutputBean implements Serializable {
	private static final long serialVersionUID = 7330572162255889514L;
	/**
     * <pre>
     * 自增序列主键
     * 表字段 : fwd_register.ID
     * </pre>
     */
    private Long id;
}
