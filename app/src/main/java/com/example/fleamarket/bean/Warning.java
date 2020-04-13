package com.example.fleamarket.bean;

import java.io.Serializable;

public class Warning implements Serializable {

    //举报人
    private User user;
    //举报内容
    private Goods goods;
    //举报信息
    private String info;
    //举报时间
    private String time;



}
