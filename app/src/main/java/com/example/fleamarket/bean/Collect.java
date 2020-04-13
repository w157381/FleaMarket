package com.example.fleamarket.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
/*
 * 我的收藏-实体类
 * */
public class Collect extends BmobObject implements Serializable {
   private String userid;
   private List<Goods> goodsList;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
