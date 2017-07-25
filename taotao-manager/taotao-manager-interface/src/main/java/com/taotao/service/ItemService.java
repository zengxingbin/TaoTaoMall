package com.taotao.service;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
public interface ItemService {
    public TbItem getItemById(Long itemId);
    public EasyUIDatagridResult getItemList(int page, int rows);
    public TaotaoResult addItem(TbItem tbItem,String desc);
    public TbItemDesc getItemDescById(Long itemId);
}
