package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService2;
import com.taotao.search.service.dao.SearchDao;
@Service
public class SearchService2Impl implements SearchService2 {
    @Autowired
    private SearchDao searchDao;
    @Override
    public SearchResult searchItem(String query, Integer page, Integer rows) {
        //创建SolrQuery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(query);
        //设置默认搜索域
        solrQuery.set("df", "item_title");
        //设置分页条件
        if(page < 1)
            page=1;
        if(rows < 1)
            rows = 60;
        solrQuery.setStart((page-1) * rows);
        solrQuery.setRows(rows);
        //设置高亮
        solrQuery.setHighlight(true);
        //添加要高亮显示的域
        solrQuery.addHighlightField("item_title");
        //设置高亮前缀
        solrQuery.setHighlightSimplePre("<font color='red'>");
        //设置高亮后缀
        solrQuery.setHighlightSimplePost("</font>");
        //调用Dao
        SearchResult result = searchDao.searchItem(solrQuery);
        //获取总记录数
        long recordCount = result.getRecordCount();
        //获取总页数
        long pages = recordCount % rows == 0 ? recordCount / rows : recordCount / rows + 1;
        //设置总页数
        result.setTotalPage(pages);
        return result;
    }

}
