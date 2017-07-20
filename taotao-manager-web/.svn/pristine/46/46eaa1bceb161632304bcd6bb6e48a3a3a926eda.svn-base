package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import 
org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemCatService;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemCatService itemCatService;
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDatagridResult getItemList(Integer page,Integer rows) {
        EasyUIDatagridResult result = itemService.getItemList(page,rows);
        return result;
    }
}
