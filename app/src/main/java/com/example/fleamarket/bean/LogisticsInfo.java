package com.example.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
/*
 * 物流信息-实体类
 * */
public class LogisticsInfo  extends BmobObject implements Serializable {
    //当前账号id
    private String LogInfo_currentUserId;
    //物流单号
    private String LogInfo_num;
    //物流公司编码
    private String LogInfo_code;
    //发货人地址
    private MyAddress LogInfo_deliverInfo;
    //收货人地址
    private MyAddress LogInfo_recrInfo;

    public String getLogInfo_currentUserId() {
        return LogInfo_currentUserId;
    }

    public void setLogInfo_currentUserId(String logInfo_currentUserId) {
        LogInfo_currentUserId = logInfo_currentUserId;
    }

    public String getLogInfo_num() {
        return LogInfo_num;
    }

    public void setLogInfo_num(String logInfo_num) {
        LogInfo_num = logInfo_num;
    }

    public String getLogInfo_code() {
        return LogInfo_code;
    }

    public void setLogInfo_code(String logInfo_code) {
        LogInfo_code = logInfo_code;
    }

    public void setLogInfo_deliverInfo(MyAddress logInfo_deliverInfo) {
        LogInfo_deliverInfo = logInfo_deliverInfo;
    }


    public MyAddress getLogInfo_recrInfo() {
        return LogInfo_recrInfo;
    }

    public void setLogInfo_recrInfo(MyAddress logInfo_recrInfo) {
        LogInfo_recrInfo = logInfo_recrInfo;
    }
}
