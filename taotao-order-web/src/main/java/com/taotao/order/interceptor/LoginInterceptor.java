package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;

public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${LOGIN_URL}")
    private String LOGIN_URL;
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //执行handler,之前调用此方法
        //从Cookie中获取token
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY, true);
        if(StringUtils.isBlank(token)) {
            //不存在此token，说明用户未登录，跳转到用户登录页面后，进行登录后再重定向到订单页面
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(LOGIN_URL + "/user/page/login?url=" + requestURL);
            return false;
        }
        //根据token从redis中取用户信息
        TaotaoResult result = userService.getUserByTokenn(token);
        if(result.getStatus() != 200) {
            //不存在用户信息
          //取当前请求的url
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(LOGIN_URL + "/user/page/login?url=" + requestURL);
            return false;
        }
        TbUser user  = (TbUser)result.getData();
        //取到用户信息，将用户信息放进request中
        request.setAttribute("user", user);
        return true;//放行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //执行handler之后，ModelAndView返回之前调用此方法
        
    }   

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //返回ModelAndView，异常处理

    }

}
