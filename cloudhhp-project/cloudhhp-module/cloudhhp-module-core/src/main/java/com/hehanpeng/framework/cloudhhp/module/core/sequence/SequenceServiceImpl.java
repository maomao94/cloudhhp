package com.hehanpeng.framework.cloudhhp.module.core.sequence;

import com.hehanpeng.framework.cloudhhp.common.util.IdWorker;
import com.hehanpeng.framework.cloudhhp.common.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Component
public class SequenceServiceImpl implements SequenceService, InitializingBean {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private int workId;

    private int dataCenterId;

    private Map<String, IdWorker> idWorkerMap = new HashMap<String, IdWorker>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        //取值0-15 (4位  0000-1111)
        int appNo = Integer.parseInt(metadata.get("AppNo"));
        //取值0-15 (4位  0000-1111)
        int workNo = Integer.parseInt(metadata.get("WorkNo"));
        //拼接成8位二进制字符串   appNo+workNo(0-255)
        String id = Integer.toBinaryString(appNo) + StringUtil.fillString(Integer.toBinaryString(workNo), 4);
        //8位二进制转成十进制
        workId = Integer.parseInt(new BigInteger(id, 2).toString());
        dataCenterId = Integer.parseInt(metadata.get("GroupNo"));
    }

    /**
     * 生成序列号
     *
     * @param seqType 序列号类型
     * @return 序列号
     */
    public Long nextId(String seqType) {
        IdWorker idWorker = idWorkerMap.get(seqType);
        if (idWorker == null) {
            synchronized (this) {
                idWorker = new IdWorker(workId, dataCenterId);
                idWorkerMap.put(seqType, idWorker);
            }
        }
        return idWorker.nextId();
    }
}
