package com.example.fleamarket.utils;

import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

//TODO 集成：1.6、自定义消息接收器处理在线消息和离线消息
public class DemoMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        Log.e("DemoMessageHandler:","getConversationId:"+event.getConversation().getConversationId());
        Log.e("DemoMessageHandler:","getConversationTitle:"+event.getConversation().getConversationTitle());
        Log.e("DemoMessageHandler:","getConversationIcon:"+event.getConversation().getConversationIcon());

        Log.e("DemoMessageHandler:","getUserId:"+event.getFromUserInfo().getUserId());
        Log.e("DemoMessageHandler:","getName:"+event.getFromUserInfo().getName());
        Log.e("DemoMessageHandler:","getAvatar:"+event.getFromUserInfo().getAvatar());
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        //在线消

    }
}
