package com.hehanpeng.framework.cloudhhp.module.forward.constant;

/**
 * @author hehanpeng
 * 2018/9/17 15:22
 */
public class ForwardParamConstant {
    /**
     * <pre>
     * 设置：STATUS状态
     * 0：未执行
     * 表字段：tto_inf.STATUS
     *
     * </pre>
     */
    public static final String TTOINF_STATUS_UNDEAL = "0";
    /**
     * <pre>
     * 设置：STATUS状态
     * 1：已执行
     * 表字段：tto_inf.STATUS
     *
     * </pre>
     */
    public static final String TTOINF_STATUS_DEALED = "1";

    /**
     * <pre>
     * TYPE（全局/局部）
     * 0:全局
     * 表字段 : tto_inf.TYPE
     * </pre>
     */
    public static final String TTOINF_TYPE_GLOBAL = "0";
    /**
     * <pre>
     * TYPE（全局/局部）
     * 1：局部
     * 表字段 : tto_inf.TYPE
     * </pre>
     */
    public static final String TTOINF_TYPE_LOCAL = "1";
    /**
     * <pre>
     * 业务类型
     * 表字段 : tto_inf.BIZ_TYPE
     * </pre>
     */
    public static final String TTOINF_BIZTYPE_DEFAULT = "00900";
    
    /**
     * <pre>
     * 业务类型
     * 表字段 : fwd_register.BIZ_TYPE
     * </pre>
     */
    public static final String FWDREG_BIZTYPE_DEFAULT = "00900";
    
    /**
     * <pre>
     * STATUS 等待W
     * 表字段：fwd_register.STATUS
     * </pre>
     */
    public static final String FWDREG_STATUS_WAIT = "W";
    /**
     * <pre>
     * STATUS 转发D中
     * 表字段：fwd_register.STATUS
     * </pre>
     */
    
    public static final String FWDREG_STATUS_FORWARD = "D";
   
    /**
     * <pre>
     * STATUS 转发次数达上限
     * 表字段：fwd_register.STATUS
     * </pre>
     */
    
    public static final String FWDREG_STATUS_UPPERLIMIT = "U";
    /**
     * <pre>
     * STATUS 撤销C
     * 表字段：fwd_register.STATUS
     * </pre>
     */
    
    public static final String FWDREG_STATUS_CANCEL = "C";
}
