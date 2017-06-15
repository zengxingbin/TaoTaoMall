package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

public interface ItemCatService {
    public List<EasyUITreeNode> getTbItemCatList(long parentId);
}
