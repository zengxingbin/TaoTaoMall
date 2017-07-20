package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import com.taotao.utils.JsonUtils;
@Controller
public class IndexController {
    //注入值
    @Value("${AD1_CATEGROY_ID}")
    private long AD1_CATEGROY_ID;
    @Value("${HEIGHT}")
    private Integer HEIGHT;
    @Value("${WIDTH}")
    private Integer WIDTH;
    @Value("${HEIGHTB}")
    private Integer HEIGHTB;
    @Value("${WIDTHB}")
    private Integer WIDTHB;
    @Autowired
    private ContentService contentService;
    @RequestMapping("/index")
    public String toIndex(Model model) {
        //根据categoryId查询内容列表，其中categoryId是固定的
        List<TbContent> list = contentService.geTbContentListByCid(AD1_CATEGROY_ID);
        //创建AD1Node列表
        List<Ad1Node>  ad1NodeList = new ArrayList<>();
        //把list转换为ad1NodeList
        for(TbContent tbContent : list) {
            //创建Ad1Node
            Ad1Node ad1Node = new Ad1Node();
            ad1Node.setAlt(tbContent.getTitle());
            ad1Node.setHeight(HEIGHT);
            ad1Node.setHeightB(HEIGHTB);
            ad1Node.setWidth(WIDTH);
            ad1Node.setWidthB(WIDTHB);
            ad1Node.setHref(tbContent.getUrl());
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());
            //加入到列表中
            ad1NodeList.add(ad1Node);
        }
        //转成json字符串
        String json = JsonUtils.objectToJson(ad1NodeList);
        model.addAttribute("ad1", json);
        return "index";
    }
}
