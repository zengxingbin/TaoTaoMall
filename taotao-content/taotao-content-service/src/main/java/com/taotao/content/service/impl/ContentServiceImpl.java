package com.taotao.content.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.utils.JsonUtils;
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    //注入jedisClient
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;
    @Override
    public EasyUIDatagridResult queryContent(long id, int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //设置查询条件
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);
        //执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);
        //通过pageInfo取分页信息
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        //创建EasyUIDataGridResult
        EasyUIDatagridResult result = new EasyUIDatagridResult();
        //设置属性
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }
    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到数据库中
        tbContentMapper.insert(content);
      //同步缓存,把数据库中修改过的内容从缓存里删了
        try {
            jedisClient.hdel(INDEX_CONTENT, content.getCategoryId()+"");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return TaotaoResult.ok();
    }
    @Override
    public TaotaoResult editContent(TbContent content) {
        //补全pojo的属性
        content.setUpdated(new Date());
        //更新到到数据库中
        tbContentMapper.updateByPrimaryKey(content);
        //同步缓存,把数据库中修改过的内容从缓存里删了
        try {
            jedisClient.hdel(INDEX_CONTENT, content.getCategoryId()+"");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return TaotaoResult.ok();
    }
    @Override
    public TaotaoResult deleteContent(List<Long> ids) {
        //创建删除条件
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        //删除
        tbContentMapper.deleteByExample(example);
        return TaotaoResult.ok();
    }
    @Override
    public List<TbContent> geTbContentListByCid(long cid) {
        //查询前先查询缓存，查询缓存异常时不能影响正常的业务逻辑，所以要try。。。catch
        try {
            String list = jedisClient.hget(INDEX_CONTENT,cid + "");
            if(StringUtils.isNotBlank(list)) {//如果字符串不为空
                //转换成list集合返回
                List<TbContent> tbContentList = JsonUtils.jsonToList(list, TbContent.class);
                return tbContentList;
            }
        } catch (Exception e) {
            System.out.println("Operation Exception!");
        }
        //创建查询条件
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        //把从数据库中查询到的内容添加到缓存中
        try {
            jedisClient.hset(INDEX_CONTENT, cid + "",JsonUtils.objectToJson(list));
        } catch (Exception e) {
            System.out.println("Operation exception!");
        }
        return list;
    }

}
