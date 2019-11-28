package com.hehanpeng.framework.cloudhhp.module.core.cache;

import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.DictService;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.DistributedEvent;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.DistributedEventAnnotation;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.DistributedEventSubscriber;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.EventConstants;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@DistributedEventAnnotation(name = EventConstants.EVENT_TYPE_DICT)
public class DictServiceCacheSubscriber implements DistributedEventSubscriber {

    private static final Map<String, Map<String, String>> dictCacheMap = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private DictService dictService;

    @Override
    public void notifyEvent(DistributedEvent appEvent) {
        log.info("receive dict change event, dictCode=" + appEvent.getKey());
        if (StringUtil.isNotBlank(appEvent.getKey())) {
            dictCacheMap.remove(appEvent.getKey());
        }
    }

    public String getNameByCode(String dictCode, String dCode) {
        Map<String, String> dictCodeMap = getCodeMap(dictCode);
        if (null == dictCodeMap) {
            return null;
        }
        String dName = dictCodeMap.get(dCode);
        if (null == dName) {
            dName = dictCodeMap.get("DEFAULT");
            log.warn("dictCode={},code={},defaultName={},dictCode no mapping", dictCode, dCode, dName);
        }
        return dName;
    }

    public Map<String, String> getCodeMap(String dictCode) {
        Map<String, String> dictCodeMap = dictCacheMap.get(dictCode);
        if (null == dictCodeMap) {
            synchronized (this) {
                dictCodeMap = dictCacheMap.get(dictCode);
                if (null == dictCodeMap) {
                    dictCodeMap = dictService.getCodeMap(dictCode);
                    if (null != dictCodeMap) {
                        dictCacheMap.put(dictCode, dictCodeMap);
                    }
                }
            }
        }
        return dictCodeMap;
    }
}
