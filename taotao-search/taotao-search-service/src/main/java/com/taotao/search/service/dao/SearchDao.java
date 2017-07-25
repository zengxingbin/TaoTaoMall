package com.taotao.search.service.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

@Repository
public class SearchDao {
    //注入SolrServer
    @Autowired
    private SolrServer solrServer;
    public SearchResult searchItem(SolrQuery query) {
        //查询，获得查询结果响应对象
        try {
            QueryResponse response = solrServer.query(query);
            SolrDocumentList documentList = response.getResults();
            //创建查询结果对象
            SearchResult result = new SearchResult();
            //获取查询总记录数
            long recordCount = documentList.getNumFound();
            result.setRecordCount(recordCount);
            //创建SearchItem的list集合
            List<SearchItem> itemList = new ArrayList<>();
            //遍历documentList
            for(SolrDocument doc : documentList) {
                //创建SearchItem对象
                SearchItem item = new SearchItem();
                item.setId((String)doc.get("id"));
                item.setItem_sell_point((String)doc.get("item_sell_point"));
                item.setItem_category_name((String)doc.get("item_category_name"));
                item.setItem_desc((String)doc.get("item_desc"));
                //有多张图片的地址的时候，只取一张图片
                String image = (String)doc.get("item_image");
                if(StringUtils.isNotBlank(image));
                    item.setItem_image(image.split(",")[0]);
                item.setItem_price((long)doc.get("item_price"));
                //获取高亮
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
                List<String> list = highlighting.get(doc.get("id")).get("item_title");
                String item_title = "";
                if(list != null && list.size() > 0) 
                    item_title = list.get(0);
                else 
                    item_title = (String) doc.get("item_title");
                item.setItem_title(item_title);
                //把SearchItem添加到list集合中
                itemList.add(item);
            }
            //将list封装到SearchReslut
            result.setItemList(itemList);
            return result;
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
