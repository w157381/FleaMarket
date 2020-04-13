package com.example.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class BulletinText extends BmobObject implements Serializable {

    private String title;
    private String goods_objectId;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoods_objectId() {
        return goods_objectId;
    }

    public void setGoods_objectId(String goods_objectId) {
        this.goods_objectId = goods_objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
