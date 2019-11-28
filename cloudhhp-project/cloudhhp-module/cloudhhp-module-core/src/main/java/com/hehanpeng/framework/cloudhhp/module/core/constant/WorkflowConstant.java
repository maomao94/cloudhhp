package com.hehanpeng.framework.cloudhhp.module.core.constant;

public class WorkflowConstant {

    /***********************     工作流节点状态     **************************/
    /** 开始节点 */
    public static final String WORK_START = "START";

    /** 处理失败 */
    public static final String PROCESS_FAILED = "FAILED";

    /** 处理超时 */
    public static final String PROCESS_TIMEOUT = "TIMEOUT";

    /** 处理等待 */
    public static final String PROCESS_WAIT = "WAIT";

    /** 继续处理 */
    public static final String PROCESS_CONTINUE = "CONTINUE";

    /** 成功 */
    public static final String PROCESS_SUCCESS = "SUCCESS";

    /** 处理中 */
    public static final String PROCESSING = "PROCESSING";

    /** 全局超时分支 */
    public static final String PROCESS_GLOBAL = "GLOBAL";

    /** 存储转发分支 */
    public static final String PROCESS_FORWARD = "FORWARD";

    /***********************     工作流上下文交易状态     **************************/

    public static final String TIMEOUT_WORKFLOW_SUFFIX = "_TIMEOUT";

    public static final String REFUND_WORKFLOW_SUFFIX = "_REFUND";

    public static final String REVOKE_WORKFLOW_SUFFIX = "_REVOKE";

    public static final String PROCESS_SKIP = "SKIP";

    /** 交易执行中 */
    public static String CONTEXT_STATUS_PENDDING = "P";

    /** 交易等待执行 */
    public static String CONTEXT_STATUS_WAITING = "W";

    /** 交易成功 */
    public static String CONTEXT_STATUS_SUCCESS = "S";

    /** 交易失败 */
    public static String CONTEXT_STATUS_FAILED = "F";
}
