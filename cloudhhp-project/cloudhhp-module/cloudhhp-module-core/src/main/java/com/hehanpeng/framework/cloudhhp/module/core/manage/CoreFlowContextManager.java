package com.hehanpeng.framework.cloudhhp.module.core.manage;

import com.hehanpeng.framework.cloudhhp.module.core.dao.core.CoreFlowContextMapper;
import com.hehanpeng.framework.cloudhhp.module.core.domain.entity.core.CoreFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CoreFlowContextManager {

    @Autowired(required = false)
    CoreFlowContextMapper coreFlowContextMapper;

    public CoreFlowContext selectContext(Long idContext) {
        return coreFlowContextMapper.selectByPrimaryKey(idContext);
    }

    public int insertContext(CoreFlowContext context) {
        return coreFlowContextMapper.insert(context);
    }

    public int updateContext(CoreFlowContext context) {
        return coreFlowContextMapper.updateByPrimaryKey(context);
    }
}
