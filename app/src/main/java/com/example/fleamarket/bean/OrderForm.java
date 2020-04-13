package com.example.fleamarket.bean;
/*
 * 订单-实体类
 * */

import cn.bmob.v3.BmobObject;

public class OrderForm extends BmobObject {
    //买家id
    private String bill_buyUserId;
    //卖家id
    private String bill_sellUserId;
    //买家信息
    private User bill_buyUserInfo;
    //卖家信息
    private User bill_sellUserInfo;
    //买家地址
    private MyAddressitem bill_buyAddr;
    //卖家地址
    private MyAddressitem bill_sellAddr;
    //商品信息
    private Goods bill_goodsInfo;
    //物流单号
    private String bill_LogInfNum;
    //物流公司编码
    private String bill_LogInftype;
    //备注
    private String bill_remark;
    //付款方式
    private String bill_paytype;
    //付款金额
    private String bill_paynum;
    //付款状态
    private boolean bill_payState;
    //发货状态 0:待发货  1：已发货  2:已收货
    private int bill_fahuoState;
    //订单状态 -1:交易失败  0：交易中 1：交易成功
    private int bill_state;
    //订单买家显示
    private boolean dis_buy;
    //订单买家显示
    private boolean dis_sell;

    public String getBill_buyUserId() {
        return bill_buyUserId;
    }

    public void setBill_buyUserId(String bill_buyUserId) {
        this.bill_buyUserId = bill_buyUserId;
    }

    public String getBill_sellUserId() {
        return bill_sellUserId;
    }

    public void setBill_sellUserId(String bill_sellUserId) {
        this.bill_sellUserId = bill_sellUserId;
    }

    public User getBill_buyUserInfo() {
        return bill_buyUserInfo;
    }

    public void setBill_buyUserInfo(User bill_buyUserInfo) {
        this.bill_buyUserInfo = bill_buyUserInfo;
    }

    public User getBill_sellUserInfo() {
        return bill_sellUserInfo;
    }

    public void setBill_sellUserInfo(User bill_sellUserInfo) {
        this.bill_sellUserInfo = bill_sellUserInfo;
    }

    public MyAddressitem getBill_buyAddr() {
        return bill_buyAddr;
    }

    public void setBill_buyAddr(MyAddressitem bill_buyAddr) {
        this.bill_buyAddr = bill_buyAddr;
    }

    public MyAddressitem getBill_sellAddr() {
        return bill_sellAddr;
    }

    public void setBill_sellAddr(MyAddressitem bill_sellAddr) {
        this.bill_sellAddr = bill_sellAddr;
    }

    public Goods getBill_goodsInfo() {
        return bill_goodsInfo;
    }

    public void setBill_goodsInfo(Goods bill_goodsInfo) {
        this.bill_goodsInfo = bill_goodsInfo;
    }

    public String getBill_LogInfNum() {
        return bill_LogInfNum;
    }

    public void setBill_LogInfNum(String bill_LogInfNum) {
        this.bill_LogInfNum = bill_LogInfNum;
    }

    public String getBill_LogInftype() {
        return bill_LogInftype;
    }

    public void setBill_LogInftype(String bill_LogInftype) {
        this.bill_LogInftype = bill_LogInftype;
    }

    public String getBill_remark() {
        return bill_remark;
    }

    public void setBill_remark(String bill_remark) {
        this.bill_remark = bill_remark;
    }

    public boolean isBill_payState() {
        return bill_payState;
    }

    public void setBill_payState(boolean bill_payState) {
        this.bill_payState = bill_payState;
    }

    public int getBill_fahuoState() {
        return bill_fahuoState;
    }

    public void setBill_fahuoState(int bill_fahuoState) {
        this.bill_fahuoState = bill_fahuoState;
    }

    public int getBill_state() {
        return bill_state;
    }

    public void setBill_state(int bill_state) {
        this.bill_state = bill_state;
    }

    public String getBill_paytype() {
        return bill_paytype;
    }

    public void setBill_paytype(String bill_paytype) {
        this.bill_paytype = bill_paytype;
    }

    public String getBill_paynum() {
        return bill_paynum;
    }

    public void setBill_paynum(String bill_paynum) {
        this.bill_paynum = bill_paynum;
    }

    public boolean isDis_buy() {
        return dis_buy;
    }

    public void setDis_buy(boolean dis_buy) {
        this.dis_buy = dis_buy;
    }

    public boolean isDis_sell() {
        return dis_sell;
    }

    public void setDis_sell(boolean dis_sell) {
        this.dis_sell = dis_sell;
    }
}
