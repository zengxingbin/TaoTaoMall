package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService2 {
    public SearchResult searchItem(String query,Integer page,Integer rows);
}   
