package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class FreeMarkerController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @RequestMapping("/gethtml")
    @ResponseBody
    public String getHtml() throws Exception {
        //获取Configuration对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获取模板
        Template template = configuration.getTemplate("hello.ftl");
        //创建数据集
        Map data = new HashMap<>();
        data.put("hello", "hello freemarker");
        //创建writer对象，并指定目标文件路径以及文件名
        Writer writer = new FileWriter(new File("C:\\Users\\Administrator\\Desktop\\hello.html"));
        template.process(data, writer);
        //关闭流资源
        writer.close();
        return "OK";
    }
}
