package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {
    //注入service
    @Autowired
    private ContentService contentService;
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDatagridResult queryContent(long categoryId,int page,int rows) {
        EasyUIDatagridResult result = contentService.queryContent(categoryId, page, rows);
        return result;
    }
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content) {
        TaotaoResult result = contentService.addContent(content);
        return result;
    }
    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult editContent(TbContent content) {
        TaotaoResult result = contentService.editContent(content);
        return result;
    }
    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String ids) {
        String[] idss = ids.split(",");
        //创建list集合装id
        List<Long> list = new ArrayList<>();
        for(String id : idss) {
            list.add(Long.parseLong(id));
        }
        //调用服务
        TaotaoResult result = contentService.deleteContent(list);
        return result;
    }
}
