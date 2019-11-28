package com.wf.ew.api.base;

import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * ApiController基类
 * Created by wangfan on 2019-05-06 上午 9:26.
 */
public class BaseApiController {

    /**
     * 获取当前登录的userId
     */
    public Integer getLoginUserId(HttpServletRequest request) {
        Token token = getLoginToken(request);
        return token == null ? null : Integer.parseInt(token.getUserId());
    }

    /**
     * 获取当前的token
     */
    public Token getLoginToken(HttpServletRequest request) {
        return SubjectUtil.getToken(request);
    }
}
