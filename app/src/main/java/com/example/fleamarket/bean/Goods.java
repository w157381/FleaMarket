package com.example.fleamarket.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject implements Serializable {

    //发布人信息1
    private User goods_user;
    //发布人id
    private String userId;
    //商品类别2
    private String goods_type;
    //商品全称
    private String goods_name;
    //商品介绍信息3
    private String goods_info;
    //商品照片集4
    private List<String> goods_imgs;
    //地理位置5
    private String goods_loca;
    //开个价6
    private int goods_price;
    //开个价7
    private int goods_inPrice;
    //开个价8
    private int goods_post;
    //留言列表9
    private List<MessageContent> goods_messCon;
    //点赞列表10
    private List<User> goods_like;
    //我想要11
    private List<User> goods_want;
    //商品状态 -2:删除 -1:下架，0：售出，1：出售中  2:待审核 ,3 :已驳回
    private int goods_state;

    public User getGoods_user() {
        return goods_user;
    }

    public void setGoods_user(User goods_user) {
        this.goods_user = goods_user;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(String goods_info) {
        this.goods_info = goods_info;
    }

    public List<String> getGoods_imgs() {
        return goods_imgs;
    }

    public void setGoods_imgs(List<String> goods_imgs) {
        this.goods_imgs = goods_imgs;
    }

    public String getGoods_loca() {
        return goods_loca;
    }

    public void setGoods_loca(String goods_loca) {
        this.goods_loca = goods_loca;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_inPrice() {
        return goods_inPrice;
    }

    public void setGoods_inPrice(int goods_inPrice) {
        this.goods_inPrice = goods_inPrice;
    }

    public int getGoods_post() {
        return goods_post;
    }

    public void setGoods_post(int goods_post) {
        this.goods_post = goods_post;
    }

    public List<MessageContent> getGoods_messCon() {
        return goods_messCon;
    }

    public void setGoods_messCon(List<MessageContent> goods_messCon) {
        this.goods_messCon = goods_messCon;
    }

    public List<User> getGoods_like() {
        return goods_like;
    }

    public void setGoods_like(List<User> goods_like) {
        this.goods_like = goods_like;
    }

    public List<User> getGoods_want() {
        return goods_want;
    }

    public void setGoods_want(List<User> goods_want) {
        this.goods_want = goods_want;
    }

    public int getGoods_state() {
        return goods_state;
    }

    public void setGoods_state(int goods_state) {
        this.goods_state = goods_state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }
}
