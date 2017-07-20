package com.taotao.search.mapper;

import java.util.List;

import com.taotao.common.pojo.SearchItem;

public interface SearchItemMapper {
    public List<SearchItem> getItemList();
    public SearchItem getItemById(long itemId);
}
