package com.example.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class BrowsGoodsHistory extends BmobObject implements Serializable {
    private String userId;
    private Goods goods_info;
    private User goodsUser_info;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Goods getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(Goods goods_info) {
        this.goods_info = goods_info;
    }

    public User getGoodsUser_info() {
        return goodsUser_info;
    }

    public void setGoodsUser_info(User goodsUser_info) {
        this.goodsUser_info = goodsUser_info;
    }
}
