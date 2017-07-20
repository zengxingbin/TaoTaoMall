package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import com.taotao.search.service.SearchService2;

@Controller
public class SearchController {
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;
    @Autowired
    private SearchService2 searchService;
    @RequestMapping("/search")
    public String search(@RequestParam("q")String query,
            @RequestParam(defaultValue="1")Integer page,Model model) {
        //解决get方式中文乱码，方式有二，一修改tomcat配置文件，目前不可行，采用方式二
        try {
            query = new String(query.getBytes("iso8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SearchResult result = searchService.searchItem(query, page, SEARCH_RESULT_ROWS);
        //将数据封装到Model中
        model.addAttribute("query", query);
        model.addAttribute("totalPages", result.getTotalPage());
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("page", page);
        return "search";
    }
}
