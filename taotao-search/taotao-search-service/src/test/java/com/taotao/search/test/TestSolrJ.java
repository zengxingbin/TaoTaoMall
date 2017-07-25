package com.taotao.search.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
   @Test
   public void testAddDocument() throws SolrServerException, IOException {
       //创建solrServer，明确solrServer的URL
       SolrServer solrServer = new HttpSolrServer("http://192.168.19.99:8080/solr");
       //创建文档库
       SolrInputDocument document = new SolrInputDocument();
       document.addField("id", "456");
       document.addField("item_title", "new2 - 阿尔卡特 (OT-927) 炭黑 联通3G手机 双卡双待");
       document.addField("item_sell_point", "哈哈哈哈");
       //添加文档库
       solrServer.add(document);
       //提交
       solrServer.commit();
   }
   //更新和添加其实是一回事，只不过更新的时候，id域不能被修改，否则就变成了添加
   @Test
   public void testDeleteDocument() throws SolrServerException, IOException {
      //创建solrServer，明确solr服务器地址
       SolrServer solrServer = new HttpSolrServer("http://192.168.19.99:8080/solr");
       solrServer.deleteById("123");
       solrServer.commit();
   }
   @Test
   public void testDeleteDocument2() throws SolrServerException, IOException {
       //创建SolrServer，明确solr服务器地址
       SolrServer solrServer = new HttpSolrServer("http://192.168.19.99:8080/solr");
       solrServer.deleteByQuery("id:456");
       solrServer.commit();
   }
   @Test
   public void testQueryDocument() throws SolrServerException {
     //创建一个SolrServer对象
       SolrServer solrServer = new HttpSolrServer("http://192.168.19.99:8080/solr/collection1");
       //创建一个SolrQuery对象
       SolrQuery query = new SolrQuery();
       //设置查询条件、过滤条件、分页条件、排序条件、高亮
       //query.set("q", "*:*");
       query.setQuery("手机");
       //分页条件
       query.setStart(0);
       query.setRows(10);
       //设置默认搜索域
       query.set("df", "item_keywords");
       //设置高亮
       query.setHighlight(true);
       //高亮显示的域
       query.addHighlightField("item_title");
       query.setHighlightSimplePre("<div>");
       query.setHighlightSimplePost("</div>");
       //执行查询，得到一个Response对象
       QueryResponse response = solrServer.query(query);
       //取查询结果
       SolrDocumentList solrDocumentList = response.getResults();
       //取查询结果总记录数
       long totalCount = solrDocumentList.getNumFound();
       System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
       long pages = totalCount%10==0?totalCount/10 : totalCount/10 + 1;
       System.out.println("总页数: " + pages);
       for (SolrDocument solrDocument : solrDocumentList) {
           System.out.println(solrDocument.get("id"));
           //取高亮显示
           Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
           List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
           String itemTitle = "";
           if (list != null && list.size() >0) {
               itemTitle = list.get(0);
           } else {
               itemTitle = (String) solrDocument.get("item_title");
           }
           System.out.println(itemTitle);
           System.out.println(solrDocument.get("item_sell_point"));
           System.out.println(solrDocument.get("item_price"));
           System.out.println(solrDocument.get("item_image"));
           System.out.println(solrDocument.get("item_category_name"));
           System.out.println("=============================================");
       }
   }
   @Test
   public void testSolrCloudAddDocument() throws SolrServerException, IOException {
       //创建CloudSolrServer，明确注册中心（zookeeper）的地址列表
       CloudSolrServer cloudSolr = new CloudSolrServer("192.168.19.99:2182,192.168.19.99:2183,192.168.19.99:2184");
       //设置默认的collection
       cloudSolr.setDefaultCollection("collection2");
       //创建文档库
       SolrInputDocument doc = new SolrInputDocument();
       //添加域
       doc.addField("id", "test0001");
       doc.addField("item_title","测试商品名称");
       doc.addField("item_price","1000");
       //把文档添加到索引库中
       cloudSolr.add(doc);
       //提交
       cloudSolr.commit();
   }
}
