package com.example.fleamarket.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.fleamarket.bean.User;
import com.zia.toastex.ToastEx;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;

import cn.bmob.v3.exception.BmobException;

public class ConnectUtil {

    public static void connect(final Context context, final User user) {
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getUser_stuNumber(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //连接成功
                        BmobIMUserInfo info = new BmobIMUserInfo();
                        info.setUserId(user.getUser_stuNumber());
                        info.setName(user.getUser_nickName());
                        info.setAvatar(user.getUser_headImg());
                        BmobIM.getInstance().updateUserInfo(info);
                        Log.e("connect","服务器连接成功！"+BmobIM.getInstance().getCurrentUid());
                    } else {
                        //连接失败
                        Log.e("connect","服务器连接失败！"+BmobIM.getInstance().getCurrentUid());
                    }
                }
            });
        }
    }
}

