package com.example.fleamarket.bean;

import java.io.Serializable;

public class ShopList implements Serializable {
    private String shopAddr;

    private String shopType;

    private String shopPrice;

    private String shopTitle;

    private String shopImg;

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getShopAddr() {
        return this.shopAddr;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopType() {
        return this.shopType;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopPrice() {
        return this.shopPrice;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getShopTitle() {
        return this.shopTitle;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopImg() {
        return this.shopImg;
    }
}
