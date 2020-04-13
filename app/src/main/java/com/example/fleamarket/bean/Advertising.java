package com.example.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
/*
 * 广告信息-实体类
 * */
public class Advertising extends BmobObject implements Serializable {
    private String adv_title;
    private String adv_imgurl;
    private String adv_url;

    public String getAdv_title() {
        return adv_title;
    }

    public void setAdv_title(String adv_title) {
        this.adv_title = adv_title;
    }

    public String getAdv_imgurl() {
        return adv_imgurl;
    }

    public void setAdv_imgurl(String adv_imgurl) {
        this.adv_imgurl = adv_imgurl;
    }

    public String getAdv_url() {
        return adv_url;
    }

    public void setAdv_url(String adv_url) {
        this.adv_url = adv_url;
    }
}
