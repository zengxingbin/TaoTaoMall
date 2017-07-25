package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
    //注入商品服务
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String showItemById(@PathVariable Long itemId,Model model) {
        //根据id查询商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
        //创建Item对象
        Item item = new Item(tbItem);
        //根据商品id查询商品描述信息
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        //返回逻辑视图
        return "item";
    }
}
