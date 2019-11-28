package com.hehanpeng.framework.cloudhhp.common.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MqMessage implements Serializable {
    private static final long serialVersionUID = 8208214338324067771L;
    /**
     * 全局ID
     */
    private String logId;

    /**
     * coreID
     */
    private Long coreId;

    /**
     * 交易标识
     */
    private String transnetId;

    /**
     * 交易标识
     */
    private String bizType;

    /**
     * desc
     */
    private String desc;
}
