package com.hehanpeng.framework.cloudhhp.module.core.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.DictService;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictCodeDto;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictDto;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.DistributedEvent;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.DistributedEventBroadcaster;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.EventConstants;
import com.hehanpeng.framework.cloudhhp.module.core.dao.core.DictCodeMapper;
import com.hehanpeng.framework.cloudhhp.module.core.dao.core.DictMapper;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.Dict;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.DictCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description:
 * @date 2019/10/31
 */
@Slf4j
@Service(version = "${demo.service.version}")
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictCodeMapper dictCodeMapper;

    /*
     * 更新通知发布器
     */
    @Autowired
    private DistributedEventBroadcaster eventBroadcaster;

    @Override
    public void addDict(DictDto dictDto) {
        Dict dict = dictMapper.selectByPrimaryKey(dictDto.getDictCode());
        if (null == dict) {
            Dict dictDB = new Dict();
            BeanUtils.copyProperties(dictDto, dictDB);
            dictMapper.insertSelective(dictDB);
        }
        List<DictCodeDto> dictCodeDtoList = dictDto.getDictCodeDtoList();
        if (null == dictCodeDtoList || dictCodeDtoList.isEmpty()) {
            return;
        }

        Example example = null;
        for (DictCodeDto dictCodeDto : dictCodeDtoList) {
            example = new Example(DictCode.class);
            example.createCriteria()
                    .andEqualTo("dictCode", dictCodeDto.getDictCode())
                    .andEqualTo("code", dictCodeDto.getCode());
            DictCode db = new DictCode();
            BeanUtils.copyProperties(dictCodeDto, db);
            if (0 == dictCodeMapper.updateByExampleSelective(db, example)) {
                dictCodeMapper.insertSelective(db);
            }
        }
        publishDictChangeEvent(dictDto.getDictCode());
    }

    @Override
    public void addDictList(List<DictDto> dictDtoList) {
        for (DictDto dictDto : dictDtoList) {
            addDict(dictDto);
        }
    }

    @Override
    public void updateDict(DictDto dictDto) {
        Dict dictDB = new Dict();
        BeanUtils.copyProperties(dictDto, dictDB);
        dictMapper.updateByPrimaryKeySelective(dictDB);
    }

    @Override
    public void updateDictCode(DictCodeDto dictCodeDto) {
        Example example = new Example(DictCode.class);
        example.createCriteria()
                .andEqualTo("dictCode", dictCodeDto.getDictCode())
                .andEqualTo("code", dictCodeDto.getCode());
        DictCode db = new DictCode();
        BeanUtils.copyProperties(dictCodeDto, db);
        dictCodeMapper.updateByExampleSelective(db, example);
        publishDictChangeEvent(dictCodeDto.getDictCode());
    }

    @Override
    public void deleteDict(String dictCode) {
        dictMapper.deleteByPrimaryKey(dictCode);
        Example example = new Example(DictCode.class);
        example.createCriteria()
                .andEqualTo("dictCode", dictCode);
        dictCodeMapper.deleteByExample(example);
        publishDictChangeEvent(dictCode);
    }

    @Override
    public void deleteDictCode(String dictCode, String dCode) {
        Example example = new Example(DictCode.class);
        example.createCriteria()
                .andEqualTo("dictCode", dictCode)
                .andEqualTo("code", dCode);
        dictCodeMapper.deleteByExample(example);
        publishDictChangeEvent(dictCode);
    }

    @Override
    public DictDto getDict(String dictCode) {
        Dict dict = dictMapper.selectByPrimaryKey(dictCode);
        DictDto dictDto = new DictDto();
        BeanUtils.copyProperties(dict, dictDto);
        return dictDto;
    }

    @Override
    public List<DictDto> getDictList(String dictCode) {
        return null;
    }

    @Override
    public String getNameByCode(String dictCode, String dCode) {
        return null;
    }

    @Override
    public Map<String, String> getCodeMap(String dictCode) {
        Map<String, String> codeMap = new LinkedHashMap<>();
        if (null == dictCode) {
            return codeMap;
        }
        Dict dict = dictMapper.selectByPrimaryKey(dictCode);
        if (null == dict) {
            return codeMap;
        }
        Example example = new Example(DictCode.class);
        example.createCriteria().andEqualTo("dictCode", dictCode);
        List<DictCode> dictCodeList = dictCodeMapper.selectByExample(example);
        if (null == dictCodeList || dictCodeList.isEmpty()) {
            return codeMap;
        }
        for (DictCode d : dictCodeList) {
            codeMap.put(d.getCode(), d.getName());
        }
        return codeMap;
    }

    private void publishDictChangeEvent(String dictCode) {
        if (eventBroadcaster == null) {
            return;
        }
        DistributedEvent distributedEvent = new DistributedEvent();
        distributedEvent.setEventType(EventConstants.EVENT_TYPE_DICT);
        distributedEvent.setKey(dictCode);
        eventBroadcaster.publishEvent(distributedEvent);
    }
}
