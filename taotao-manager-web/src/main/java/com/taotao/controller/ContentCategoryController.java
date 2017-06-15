package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
    //调用服务
    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name="id",defaultValue="0") long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }
    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCatetory(long parentId,String name) {
        TaotaoResult result = contentCategoryService.addContentCatetory(parentId, name);
        return result;
    }
    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult renameContentCategory(long id,String name) {
        TaotaoResult result = contentCategoryService.renameContentCategory(id, name);
        return result;
    }
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public TaotaoResult deleteContentCategory(long id) {
        TaotaoResult result = contentCategoryService.deleteContentCategory(id);
        return result;
    }
    
}

