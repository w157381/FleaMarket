package com.example.fleamarket.bean;


import java.io.Serializable;
import java.util.List;

public class Showapi_res_body implements Serializable {
    private int ret_code;

    private int allSize;

    private List<ShopList> shopList;

    private String remark;

    private int curPage;

    private int pageSize;

    private int showapi_fee_code;

    private int allPage;

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getRet_code() {
        return this.ret_code;
    }

    public void setAllSize(int allSize) {
        this.allSize = allSize;
    }

    public int getAllSize() {
        return this.allSize;
    }

    public void setShopList(List<ShopList> shopList) {
        this.shopList = shopList;
    }

    public List<ShopList> getShopList() {
        return this.shopList;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setShowapi_fee_code(int showapi_fee_code) {
        this.showapi_fee_code = showapi_fee_code;
    }

    public int getShowapi_fee_code() {
        return this.showapi_fee_code;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public int getAllPage() {
        return this.allPage;
    }
}
