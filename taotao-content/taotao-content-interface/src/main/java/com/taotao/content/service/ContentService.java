package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
    public EasyUIDatagridResult queryContent(long id,int page,int rows);
    public TaotaoResult addContent(TbContent content);
    public TaotaoResult editContent(TbContent content);
    public TaotaoResult deleteContent(List<Long> ids);
    public List<TbContent> geTbContentListByCid(long cid);
}
