package com.example.fleamarket.bean;

import java.io.Serializable;

/*
 * 点赞
 * */
public class Like implements Serializable {

    //点赞人-信息
    private User like_userInfo;
    //点赞-是否可见
    private boolean is_display;

    public User getLike_userInfo() {
        return like_userInfo;
    }

    public void setLike_userInfo(User like_userInfo) {
        this.like_userInfo = like_userInfo;
    }

    public boolean isIs_display() {
        return is_display;
    }

    public void setIs_display(boolean is_display) {
        this.is_display = is_display;
    }
}
