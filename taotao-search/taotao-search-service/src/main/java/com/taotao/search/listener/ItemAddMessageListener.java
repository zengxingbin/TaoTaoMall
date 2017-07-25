package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

public class ItemAddMessageListener implements MessageListener {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer; 
    @Override
    public void onMessage(Message message) {
        
        try {
          //把message强转成TextMessage
            TextMessage textMessage = (TextMessage) message;
            //获取商品id
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //根据商品id从数据库中查询商品信息,由于添加商品的方法事务可能还没提交到数据库，所以需等待一下，再查询数据库
            Thread.sleep(1000);
            SearchItem item = searchItemMapper.getItemById(itemId);
            //创建SolrInputDocument
            SolrInputDocument document = new SolrInputDocument();
            //添加域
            document.addField("id", item.getId());
            document.addField("item_title", item.getItem_title());
            document.addField("item_sell_point", item.getItem_sell_point());
            document.addField("item_price", item.getItem_price());
            document.addField("item_image", item.getItem_image());
            document.addField("item_category_name", item.getItem_category_name());
            //添加到索引库中
            solrServer.add(document);
            //提交
            solrServer.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
