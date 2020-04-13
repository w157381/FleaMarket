package com.example.fleamarket.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王鹏飞
 * on 2020-02-15
 */
public class User extends BmobObject implements Serializable {

    //用户类型
    private int user_type;
    //学号
    private String user_stuNumber;
    //密码
    private String user_password;
    //姓名
    private String user_name;
    //昵称
    private String user_nickName;
    //头像
    private String user_headImg;
    //个人介绍
    private String user_introduce;
    //性别
    private String user_sex;
    //出生日期
    private String user_birthday;
    //学校
    private String user_univercity;
    //学院
    private String user_department;
    //班级
    private String user_class;
    //专业
    private String user_major;
    //年级
    private String user_grade;
    //故乡
    private String user_locationLive;
    //现在地址
    private String user_locationNow;
    //用户信誉度
    private int user_credit;
    //登录状态(-1：永久冻结；0:冻结，1：正常登录)
    private int user_loginState;
    //冻结日期
    private String user_freezeDate;
    //被举报次数
    private List<Warning> user_warning;

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_stuNumber() {
        return user_stuNumber;
    }

    public void setUser_stuNumber(String user_stuNumber) {
        this.user_stuNumber = user_stuNumber;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nickName() {
        return user_nickName;
    }

    public void setUser_nickName(String user_nickName) {
        this.user_nickName = user_nickName;
    }

    public String getUser_headImg() {
        return user_headImg;
    }

    public void setUser_headImg(String user_headImg) {
        this.user_headImg = user_headImg;
    }

    public String getUser_introduce() {
        return user_introduce;
    }

    public void setUser_introduce(String user_introduce) {
        this.user_introduce = user_introduce;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_univercity() {
        return user_univercity;
    }

    public void setUser_univercity(String user_univercity) {
        this.user_univercity = user_univercity;
    }

    public String getUser_department() {
        return user_department;
    }

    public void setUser_department(String user_department) {
        this.user_department = user_department;
    }

    public String getUser_class() {
        return user_class;
    }

    public void setUser_class(String user_class) {
        this.user_class = user_class;
    }

    public String getUser_locationLive() {
        return user_locationLive;
    }

    public void setUser_locationLive(String user_locationLive) {
        this.user_locationLive = user_locationLive;
    }

    public String getUser_locationNow() {
        return user_locationNow;
    }

    public void setUser_locationNow(String user_locationNow) {
        this.user_locationNow = user_locationNow;
    }

    public int getUser_credit() {
        return user_credit;
    }

    public void setUser_credit(int user_credit) {
        this.user_credit = user_credit;
    }

    public int getUser_loginState() {
        return user_loginState;
    }

    public void setUser_loginState(int user_loginState) {
        this.user_loginState = user_loginState;
    }

    public List<Warning> getUser_warning() {
        return user_warning;
    }

    public void setUser_warning(List<Warning> user_warning) {
        this.user_warning = user_warning;
    }

    public String getUser_freezeDate() {
        return user_freezeDate;
    }

    public void setUser_freezeDate(String user_freezeDate) {
        this.user_freezeDate = user_freezeDate;
    }

    public String getUser_major() {
        return user_major;
    }

    public void setUser_major(String user_major) {
        this.user_major = user_major;
    }

    public String getUser_grade() {
        return user_grade;
    }

    public void setUser_grade(String user_grade) {
        this.user_grade = user_grade;
    }

}
