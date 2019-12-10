package com.hehanpeng.framework.cloudhhp.module.core.sequence;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LogId 总长度
 * yyyyMMddHHmmssSSS + GroupNo +AppNo +WorkNo +4位随机位
 */
@Component
public class LogIdUtil {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private AtomicLong atomicLong = new AtomicLong(0);

    private String seqString;

    private String getId() {
        if (null == seqString) {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
            seqString = metadata.get("AppNo") + metadata.get("GroupNo") + metadata.get("WorkNo");
        }
        return seqString;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return logId
     */
    public String nextId() {
        return new StringBuffer().append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())).
                append(getId()).append(String.format("%04d", atomicLong.incrementAndGet() % 10000)).toString();
    }
}
