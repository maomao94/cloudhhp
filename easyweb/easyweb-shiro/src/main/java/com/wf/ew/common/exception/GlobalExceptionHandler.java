package com.wf.ew.common.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wf.jwtp.exception.TokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理器
 * Created by wangfan on 2018-02-22 上午 11:29
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

    /**
     * 对不同错误进行不同处理
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        String url = "error/500.html", msg = "喔唷，系统出了一个小故障", error = ex.getMessage();
        int code = 500;
        if (ex instanceof IException) {
            msg = ex.getMessage();
            code = ((IException) ex).getCode();
        } else if (ex instanceof UnauthorizedException) {
            code = 403;
            msg = "抱歉，您没有访问权限";
            url = "error/403.html";
        } else if (ex instanceof TokenException) {
            msg = ex.getMessage();
            code = ((IException) ex).getCode();
            // 支持跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
            printJSON(code, msg, error, response);
            return null;
        } else {
            logger.error(ex.getMessage(), ex);
        }
        if (isAjax(request)) {
            printJSON(code, msg, error, response);
            return null;
        }
        model.addAttribute("code", code);
        model.addAttribute("msg", msg);
        model.addAttribute("error", error);
        return url;
    }

    /**
     * 判断是不是ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        String xHeader = request.getHeader("X-Requested-With");
        return (xHeader != null && xHeader.contains("XMLHttpRequest"));
    }

    /**
     * 输出json内容
     */
    private void printJSON(int code, String msg, String error, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.write("{\"code\": " + code + ", \"msg\": \"" + msg + "\", \"error\": \"" + error + "\"}");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
