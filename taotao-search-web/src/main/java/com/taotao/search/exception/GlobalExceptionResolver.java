package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver{
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception e) {
        //打印异常信息
        e.printStackTrace();
        //把一场写入日志
        logger.info("进入全局异常处理。。。！");
        logger.debug("测试handler类型。。。！",handler.getClass());
        logger.error("系统发生异常！",e);
        //通知开发人员，发短信，发邮件
        //向用户展示错误提示页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "网络异常或者当前访问人数过多，请稍后再试!");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
    
}
