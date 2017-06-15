package com.taotao.content.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
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
        return TaotaoResult.ok();
    }
    @Override
    public TaotaoResult editContent(TbContent content) {
        //补全pojo的属性
        content.setUpdated(new Date());
        //更新到到数据库中
        tbContentMapper.updateByPrimaryKey(content);
        
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
        //创建查询条件
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        return list;
    }

}
