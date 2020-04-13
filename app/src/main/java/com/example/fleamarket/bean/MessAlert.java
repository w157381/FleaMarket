package com.example.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.newim.event.MessageEvent;

public class MessAlert implements Serializable {
    private MessageEvent messageEvent;
    private String  num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }
}
