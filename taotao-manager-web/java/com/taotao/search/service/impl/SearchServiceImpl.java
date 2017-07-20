package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService {
    //注入SearchItemMapper
    @Autowired
    private SearchItemMapper searchItemMapper;
    //诸如SolrServer
    @Autowired
    private SolrServer solrServer;
    @Override
    public TaotaoResult insertItemToIndex() {
      //查询数据库，获取商品列表
        List<SearchItem> itemList = searchItemMapper.getItemList();
        try {
            //遍历商品列表添加到索引库中
            for (SearchItem item : itemList) {
                System.out.println(item);
                //创建文档库
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", item.getId());
                doc.addField("item_title", item.getItem_title());
                doc.addField("item_sell_point", item.getItem_sell_point());
                doc.addField("item_price", item.getItem_price());
                doc.addField("item_image", item.getItem_image());
                doc.addField("item_category_name", item.getItem_category_name());
                //添加文档到索引库中
                solrServer.add(doc);
            }
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
           return TaotaoResult.build(500, "导入索引库失败");
        }
        return TaotaoResult.ok();
    }
    

}
