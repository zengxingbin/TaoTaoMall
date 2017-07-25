package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping("/user/page/register")
    public String showRegister() {
        return "register";
    }
    @RequestMapping("/user/page/login")
    public String showLogin(String url, Model model) {
        //将重定向的url放进request作用域中
        model.addAttribute("redirect", url);
        return "login";
    }
}
