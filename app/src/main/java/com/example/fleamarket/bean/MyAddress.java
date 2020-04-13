package com.example.fleamarket.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/*
 * 收货地址-实体类
 * */
public class MyAddress extends BmobObject implements Serializable {
    //id
    private String addr_userId;
    //地址信息
    private List<MyAddressitem> addressitems;

    public String getAddr_userId() {
        return addr_userId;
    }

    public void setAddr_userId(String addr_userId) {
        this.addr_userId = addr_userId;
    }

    public List<MyAddressitem> getAddressitems() {
        return addressitems;
    }

    public void setAddressitems(List<MyAddressitem> addressitems) {
        this.addressitems = addressitems;
    }
}
