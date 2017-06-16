package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    TbItemMapper tbItemMapper;
    @Autowired
    TbItemDescMapper tbItemDescMapper;
    public TbItem getItemById(Long itemId) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
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
        long itemId = IDUtils.genItemId();
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
        return TaotaoResult.ok();
    }
    

}
