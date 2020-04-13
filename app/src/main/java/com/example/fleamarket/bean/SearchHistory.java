package com.example.fleamarket.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class SearchHistory extends BmobObject {
    private String userId;
    private List<String> lists;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }
}
