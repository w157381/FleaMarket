package com.example.fleamarket.bean;

import java.io.Serializable;
import java.util.List;

public class MessageContentItem implements Serializable {

    private User curr_user;
    private User to_user;
    private String content;
    private List<User> like;
    private String time;
    private boolean mess_dis;

    public User getCurr_user() {
        return curr_user;
    }

    public void setCurr_user(User curr_user) {
        this.curr_user = curr_user;
    }

    public User getTo_user() {
        return to_user;
    }

    public void setTo_user(User to_user) {
        this.to_user = to_user;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
