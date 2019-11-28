package com.hehanpeng.framework.cloudhhp.common.config.redis.appevent;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class DistributedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用事件类型
     */
    private String eventType;

    /**
     * key信息
     */
    private String key;

    /**
     * 生存时间（毫秒）
     */
    private long timeToLive;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 应用事件属性
     */
    private Map<String, Serializable> properties = new HashMap<String, Serializable>();

    /**
     * 生成时间
     */
    private Date generateTime;
}