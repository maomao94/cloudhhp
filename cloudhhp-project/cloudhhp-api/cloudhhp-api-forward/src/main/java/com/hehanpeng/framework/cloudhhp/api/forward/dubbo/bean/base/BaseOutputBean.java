package com.hehanpeng.framework.cloudhhp.api.forward.dubbo.bean.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseOutputBean implements Serializable {
    private static final long serialVersionUID = -4953447120914026405L;
    protected String responseCode;
    protected String responseMsg;
}