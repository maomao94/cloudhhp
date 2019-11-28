package com.hehanpeng.framework.cloudhhp.api.core.dubbo.service;

import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictCodeDto;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictDto;

import java.util.List;
import java.util.Map;

/**
 * @author hehanpeng
 * @version V1.0
 * @Description:
 * @date 2019/10/31
 */
public interface DictService {
    /**
     * 增加一个字典代码映射
     *
     * @param dictDto 字典代码映射
     */
    void addDict(DictDto dictDto);

    /**
     * 增加一个字典代码映射
     *
     * @param dictDtoList 字典代码映射
     */
    void addDictList(List<DictDto> dictDtoList);

    /**
     * 修改一个字典代码映射
     *
     * @param dictDto 字典代码映射
     */
    void updateDict(DictDto dictDto);

    /**
     * 修改DictCode映射
     *
     * @param dictCodeDto 字典代码映射
     */
    void updateDictCode(DictCodeDto dictCodeDto);

    /**
     * 删除一个字典代码映射
     *
     * @param dictCode 分类代码
     */
    void deleteDict(String dictCode);

    /**
     * 删除一个字典代码映射
     *
     * @param dictCode 分类代码
     * @param dCode    字典代码
     */
    void deleteDictCode(String dictCode, String dCode);

    /**
     * 获得字典定义集
     *
     * @param dictCode 分类代码模式
     * @return 字典基本定义集
     */
    DictDto getDict(String dictCode);

    /**
     * 获得字典定义集
     *
     * @param dictCode 分类代码模式
     * @return
     */
    @Deprecated
    List<DictDto> getDictList(String dictCode);

    /**
     * 通过字典代码获得对应的名称
     *
     * @param dictCode 分类代码
     * @param dCode    字典代码
     * @return 字典代码对应的名称
     */
    @Deprecated
    String getNameByCode(String dictCode, String dCode);

    /**
     * 获得分类代码下的字典代码映射
     *
     * @param dictCode 分类代码
     * @return 字典代码映射
     */
    Map<String, String> getCodeMap(String dictCode);
}
