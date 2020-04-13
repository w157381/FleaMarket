package com.example.fleamarket.bean;

import java.io.Serializable;

public class MyAddressitem implements Serializable {
    //名字
    private String addr_name;
    //联系方式
    private String addr_phone;
    //地址
    private String addr_address_a;
    private String addr_address_b;
    //类型 0：收货地址 1：发货地址
    private String addr_type;
    //默认地址
    private boolean isdefault;



    public String getAddr_name() {
        return addr_name;
    }

    public void setAddr_name(String addr_name) {
        this.addr_name = addr_name;
    }

    public String getAddr_phone() {
        return addr_phone;
    }

    public void setAddr_phone(String addr_phone) {
        this.addr_phone = addr_phone;
    }

    public String getAddr_address_a() {
        return addr_address_a;
    }

    public void setAddr_address_a(String addr_address_a) {
        this.addr_address_a = addr_address_a;
    }

    public String getAddr_address_b() {
        return addr_address_b;
    }

    public void setAddr_address_b(String addr_address_b) {
        this.addr_address_b = addr_address_b;
    }

    public String getAddr_type() {
        return addr_type;
    }

    public void setAddr_type(String addr_type) {
        this.addr_type = addr_type;
    }

    public boolean isIsdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }
}
