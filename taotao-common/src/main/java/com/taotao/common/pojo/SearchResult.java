package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
    private long totalPage;
    private long recordCount;
    private List<SearchItem> itemList;
    public long getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
    public long getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }
    public List<SearchItem> getItemList() {
        return itemList;
    }
    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
    
}
