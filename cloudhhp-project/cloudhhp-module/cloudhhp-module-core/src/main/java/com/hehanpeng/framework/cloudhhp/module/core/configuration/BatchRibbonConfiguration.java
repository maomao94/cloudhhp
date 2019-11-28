package com.hehanpeng.framework.cloudhhp.module.core.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
//@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
public class BatchRibbonConfiguration {
}
