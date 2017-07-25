package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener{
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;
    public void onMessage(Message message) {
        try {
            //监听商品添加的信息,获取商品id
            TextMessage textMessage = (TextMessage)message;
            String text = textMessage.getText();
            Long itemId =  Long.parseLong(text);
            //先等待商品添加的事务提交
            Thread.sleep(1000);
          //根据商品Id从数据库中查询商品信息
            TbItem tbItem = itemService.getItemById(itemId);
            //创建Item对象
            Item item = new Item(tbItem);
            //根据商品id查询商品详情
            TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
            //获取模板文件的Configuration对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //获取模板
            Template template = configuration.getTemplate("item.ftl");
            //创建数据集
            Map data = new HashMap<>();
            data.put("item", item);
            data.put("itemDesc", tbItemDesc);
            //创建Writer对象，指定输出的目标文件的路径和文件名
            Writer writer = new FileWriter(new File(HTML_OUT_PATH + itemId + ".html"));
            //生成静态资源
            template.process(data, writer);
            //关闭流资源
            writer.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }

}
