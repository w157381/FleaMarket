package com.example.fleamarket.bean;

import java.io.Serializable;
import java.util.List;

/*
 * 留言
 * */
public class MessageContent implements Serializable {

    private User from_user;
    private String content;
    private List<User> like;
    private String time;
    private boolean mess_dis;
    private List<MessageContentItem> list;

    public User getFrom_user() {
        return from_user;
    }

    public void setFrom_user(User from_user) {
        this.from_user = from_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getLike() {
        return like;
    }

    public void setLike(List<User> like) {
        this.like = like;
    }

    public boolean isMess_dis() {
        return mess_dis;
    }

    public void setMess_dis(boolean mess_dis) {
        this.mess_dis = mess_dis;
    }

    public List<MessageContentItem> getList() {
        return list;
    }

    public void setList(List<MessageContentItem> list) {
        this.list = list;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
