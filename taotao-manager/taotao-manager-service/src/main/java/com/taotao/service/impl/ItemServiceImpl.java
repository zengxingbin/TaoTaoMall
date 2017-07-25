package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    TbItemMapper tbItemMapper;
    @Autowired
    TbItemDescMapper tbItemDescMapper;
    //注入jmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;
    //注入Destination
    @Resource(name="itemAddTopic")
    private Destination destination;
    //注入jedis
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${EXPIRE_TIME}")
    private int EXPIRE_TIME;
    public TbItem getItemById(Long itemId) {
        //查询数据库之前先查询redis缓存
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":" + "BASE");
            //将json串转化为object
            if(StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        //缓存中不存在再查询数据库，并把查询到的商品信息放入缓存中
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        try {
            //放入缓存中
            jedisClient.set(ITEM_INFO + ":" + itemId + ":" + "BASE",JsonUtils.objectToJson(tbItem));
            //设置redis中key的失效时间
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":" + "BASE",EXPIRE_TIME);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }
    @Override
    public EasyUIDatagridResult getItemList(int page, int rows) {
      //设置分页信息
        PageHelper.startPage(page, rows);
        //查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //获取信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDatagridResult result = new EasyUIDatagridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }
    @Override
    public TaotaoResult addItem(TbItem tbItem, String desc) {
        //补全商品属性
        //创建商品id
        final long itemId = IDUtils.genItemId();
        tbItem.setCid(itemId);
        tbItem.setId(itemId);
        //添加商品状态，1-正常2-下架3-删除
        tbItem.setStatus((byte)1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //插入到数据库表中
        tbItemMapper.insert(tbItem);
        //创建TbItemDesc封装desc（商品描述）,并补全属性
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //插入到数据库表中
        tbItemDescMapper.insert(tbItemDesc);
        //添加商品后发送消息，同步索引库
        jmsTemplate.send(destination,new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送的是商品id
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
                
            }
        });
        return TaotaoResult.ok();
    }
    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //查询数据库之前先查询缓存
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":" + "DESC");
            if(StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        //根据商品商品Id查询商品描述
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //放入缓存中
        try {
            jedisClient.set(ITEM_INFO + ":" + itemId + ":" + "DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置key的过期时间
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":" + "DESC", EXPIRE_TIME);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
